package vancore.playground.hearthstone.ui

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import vancore.playground.hearthstone.data.model.Card
import vancore.playground.util.glide.GlideRequests

/**
 * A simple adapter implementation that shows Reddit posts.
 */
class CardAdapter(private val glide: GlideRequests)
    : PagingDataAdapter<Card, CardViewHolder>(POST_COMPARATOR) {

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: CardViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val item = getItem(position)
            holder.updateScore(item)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.create(parent, glide)
    }

    companion object {
        private val PAYLOAD_SCORE = Any()
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Card>() {
            override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean =
                oldItem.title == newItem.title

            override fun getChangePayload(oldItem: Card, newItem: Card): Any? {
                return if (sameExceptScore(oldItem, newItem)) {
                    PAYLOAD_SCORE
                } else {
                    null
                }
            }
        }

        private fun sameExceptScore(oldItem: Card, newItem: Card): Boolean {
            // DON'T do this copy in a real app, it is just convenient here for the demo :)
            // because reddit randomizes scores, we want to pass it as a payload to minimize
            // UI updates between refreshes
            return oldItem.copy(id = newItem.id) == newItem
        }
    }
}