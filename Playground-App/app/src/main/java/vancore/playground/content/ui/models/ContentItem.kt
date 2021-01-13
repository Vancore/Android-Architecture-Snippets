package vancore.playground.content.ui.models

data class ContentItem(
    val title: String?,
    val description: String?,
    val contentType: ContentType?
)

enum class ContentType(type: Int) {
    Type1(type = 0),
    Type2(type = 0),
    Type3(type = 0),
    Type4(type = 0)
}