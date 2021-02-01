package vancore.playground.hearthstone.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import kotlinx.android.synthetic.main.fragment_card_browser.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.collect
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vancore.playground.R
import vancore.playground.hearthstone.authentication.LoginResponse
import vancore.playground.hearthstone.ui.loadingstate.CardLoadStateAdapter
import vancore.playground.util.ServiceLocator
import vancore.playground.util.SessionManager
import vancore.playground.util.glide.GlideApp

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalPagingApi
class CardBrowserFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_card_browser, container, false)
        return view
    }

    private val model: CardBrowserViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                val repo = context?.let {
                    ServiceLocator.instance(it)
                        .getRepository()
                }
                @Suppress("UNCHECKED_CAST")
                return repo?.let { CardBrowserViewModel(it, handle) } as T
            }
        }
    }

    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: CardAdapter
    private var authToken = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        initAdapter()
        initSwipeToRefresh()
        initSearch()
    }

    private fun initAdapter() {
        val glide = GlideApp.with(this)
        adapter = CardAdapter(glide)
        rv_card_browser.adapter = adapter.withLoadStateHeaderAndFooter(
            header = CardLoadStateAdapter(adapter),
            footer = CardLoadStateAdapter(adapter)
        )

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadStates ->
                swipe_refresh.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

        ServiceLocator.instance(requireContext()).getCardApi().login("client_credentials", "LEWHj96ZC2dVT94m8jMSDHKAvrYRMSYK", "83d65959a7484ec18f48a60f2f9f4c00")
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Error logging in
                    authToken = ""
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val loginResponse = response.body()

                    if (loginResponse?.accessToken != null /*loginResponse?.statusCode == 200*/) {
                        sessionManager.saveAuthToken(loginResponse.accessToken)
                        authToken = loginResponse.accessToken

                        lifecycleScope.launchWhenCreated {
                            model.cards.collectLatest {
                                adapter.submitData(it)
                            }
                        }
                    } else {
                        // Error logging in
                    }
                }
            })

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { rv_card_browser.scrollToPosition(0) }
        }
    }

    private fun initSwipeToRefresh() {
        swipe_refresh.setOnRefreshListener { adapter.refresh() }
    }

    private fun initSearch() {
        input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updatedSubredditFromInput()
                true
            } else {
                false
            }
        }
        input.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updatedSubredditFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updatedSubredditFromInput() {
        input.text.trim().toString().let {
            if (it.isNotBlank() && model.shouldShowSubreddit(it)) {
                model.showSubreddit(it)
            }
        }
    }
}