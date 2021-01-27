package vancore.playground.hearthstone.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import vancore.playground.hearthstone.data.model.Card

/**
 * Database schema used by the DbRedditPostRepository
 */
@Database(
    entities = [Card::class],
    version = 1,
    exportSchema = false
)
abstract class CardDb : RoomDatabase() {
    companion object {
        fun create(context: Context, useInMemory: Boolean): CardDb {
            val databaseBuilder = if (useInMemory) {
                Room.inMemoryDatabaseBuilder(context, CardDb::class.java)
            } else {
                Room.databaseBuilder(context, CardDb::class.java, "card.db")
            }
            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun cards(): CardDao
}