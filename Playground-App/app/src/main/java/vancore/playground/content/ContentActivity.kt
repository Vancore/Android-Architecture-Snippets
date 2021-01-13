package vancore.playground.content

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import vancore.playground.databinding.ActivityContentBinding
import vancore.playground.content.ui.ContentAdapter
import vancore.playground.content.ui.ContentClickListener
import vancore.playground.content.ui.models.ContentItem
import vancore.playground.content.ui.models.ContentType
import vancore.playground.hearthstone.CardBrowserActivity

class ContentActivity : AppCompatActivity(), ContentClickListener {

    private lateinit var binding: ActivityContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()

        val contentList = listOf(
            ContentItem("Title 1", "Description 1", ContentType.Type1),
            ContentItem("Title 2", "Description 2", ContentType.Type2),
            ContentItem("Title 3", "Description 3", ContentType.Type3),
            ContentItem("Title 4", "Description 4", ContentType.Type4)
        )

        //I use viewbinding here, for other classes or maybe in the future: ToDo use databinding
        with(binding) {

            rvContent.adapter = ContentAdapter(contentList, this@ContentActivity)

        }

    }

    override fun onContentClicked(contentType: ContentType?) {
        val intent = Intent(this, CardBrowserActivity::class.java).apply {
            putExtra("name", "value")
        }
        this.startActivity(intent)
    }


}