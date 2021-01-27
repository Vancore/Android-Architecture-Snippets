package vancore.playground.hearthstone.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "cards",
    indices = [Index(value = ["manacost"], unique = false)]
)
data class Card(

    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("manacost")
    val manacost: String,

    @SerializedName("thumbnail")
    val thumbnail: String,

    @SerializedName("region")
    val region: String
) {
    // to be consistent w/ changing backend order, we need to keep a data like this
    var indexInResponse: Int = -1
}