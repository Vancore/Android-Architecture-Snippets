package vancore.playground.hearthstone.ui

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import vancore.playground.R
import vancore.playground.hearthstone.data.model.Card
import vancore.playground.util.glide.GlideRequests

/**
 * A RecyclerView ViewHolder that displays a reddit post.
 */
class CardViewHolder(view: View, private val glide: GlideRequests) : RecyclerView.ViewHolder(view) {

    private val title: TextView = view.findViewById(R.id.tv_card_title)
    private val manacost: TextView = view.findViewById(R.id.tv_card_manacost)
    private val description: TextView = view.findViewById(R.id.tv_card_description)
    private val thumbnail : ImageView = view.findViewById(R.id.iv_card)

    private var card : Card? = null
    init {
        view.setOnClickListener {
            card?.region?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }
        }
    }

    fun bind(card: Card?) {
        this.card = card
        title.text = card?.title ?: "loading"
        manacost.text = card?.manacost ?: "mana"
        description.text = "${card?.description ?: 0}"
        if (card?.thumbnail?.startsWith("http") == true) {
            thumbnail.visibility = View.VISIBLE
            glide.load(card.thumbnail)
                .centerCrop()
                .placeholder(R.drawable.ic_photo_24px)
                .into(thumbnail)
        } else {
            thumbnail.visibility = View.GONE
            glide.clear(thumbnail)
        }
    }

    companion object {
        fun create(parent: ViewGroup, glide: GlideRequests): CardViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.listitem_hearthstone_card, parent, false)
            return CardViewHolder(view, glide)
        }
    }

    fun updateScore(item: Card?) {
        card = item
        description.text = "${item?.manacost ?: 0}"
    }
}