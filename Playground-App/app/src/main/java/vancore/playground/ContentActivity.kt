package vancore.playground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import vancore.playground.databinding.ActivityContentBinding
import vancore.playground.ui.ContentAdapter
import vancore.playground.ui.models.ContentItem

class ContentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()

        val contentList = listOf<ContentItem>(
            ContentItem("Title 1", "Description 1"),
            ContentItem("Title 2", "Description 2"),
            ContentItem("Title 3", "Description 3"),
            ContentItem("Title 4", "Description 4")
        )

        with(binding) {

            rvContent.adapter = ContentAdapter(contentList)

        }

    }
}