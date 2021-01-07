package vancore.playground.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import vancore.playground.databinding.ListitemContentBinding
import vancore.playground.ui.models.ContentItem

class ContentAdapter(private val contentList: List<ContentItem>) : RecyclerView.Adapter<ContentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val binding = ListitemContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContentViewHolder(binding)
    }

    override fun getItemCount(): Int = contentList.size

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        with(holder){
            with(contentList[position]) {
                binding.tvContentTitle.text = title
                binding.tvContentDescription.text = description
            }

            itemView.setOnClickListener {
                Toast.makeText(holder.itemView.context, "click on position: ${position + 1}", Toast.LENGTH_SHORT).show()
            }

        }
    }
}