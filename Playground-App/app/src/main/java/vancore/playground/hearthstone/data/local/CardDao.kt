package vancore.playground.hearthstone.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import vancore.playground.hearthstone.data.model.Card

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<Card>)

    @Query("SELECT * FROM cards WHERE manacost = :manacosts")
    fun cardByManacost(manacosts: Int): PagingSource<Int, Card>

    @Query("DELETE FROM cards WHERE id = :cardId")
    suspend fun deleteBySubreddit(cardId: Int)

    @Query("SELECT MAX(indexInResponse) + 1 FROM cards WHERE id = :cardId")
    suspend fun getNextIndexInSubreddit(cardId: Int): Int
}