package vancore.playground.hearthstone.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import vancore.playground.hearthstone.data.CardRepository
import vancore.playground.hearthstone.data.model.Card

@FlowPreview
@ExperimentalCoroutinesApi
class CardBrowserViewModel(
    private val repository: CardRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val KEY_SUBREDDIT = "subreddit"
        const val DEFAULT_SUBREDDIT = "androiddev"
    }

    init {
        if (!savedStateHandle.contains(KEY_SUBREDDIT)) {
            savedStateHandle.set(KEY_SUBREDDIT, DEFAULT_SUBREDDIT)
        }
    }

    private val clearListCh = Channel<Unit>(Channel.CONFLATED)

    val cards = flowOf(
        clearListCh.receiveAsFlow().map { PagingData.empty<Card>() },
        savedStateHandle.getLiveData<String>(KEY_SUBREDDIT)
            .asFlow()
            .flatMapLatest { repository.cards(it, 30) }
            // cachedIn() shares the paging state across multiple consumers of posts,
            // e.g. different generations of UI across rotation config change
            .cachedIn(viewModelScope)
    ).flattenMerge(2)

    fun shouldShowSubreddit(
        subreddit: String
    ) = savedStateHandle.get<String>(KEY_SUBREDDIT) != subreddit

    fun showSubreddit(subreddit: String) {
        if (!shouldShowSubreddit(subreddit)) return

        clearListCh.offer(Unit)

        savedStateHandle.set(KEY_SUBREDDIT, subreddit)
    }
}