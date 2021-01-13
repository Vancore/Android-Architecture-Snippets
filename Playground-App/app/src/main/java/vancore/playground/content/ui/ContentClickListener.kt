package vancore.playground.content.ui

import vancore.playground.content.ui.models.ContentType

interface ContentClickListener {
    fun onContentClicked(contentType: ContentType?)
}