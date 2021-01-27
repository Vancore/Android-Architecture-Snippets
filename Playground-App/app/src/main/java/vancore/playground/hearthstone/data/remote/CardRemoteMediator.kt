package vancore.playground.hearthstone.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import vancore.playground.hearthstone.data.local.CardDao
import vancore.playground.hearthstone.data.local.CardDb
import vancore.playground.hearthstone.data.model.Card
import java.io.IOException

@ExperimentalPagingApi
class CardRemoteMediator(
    private val db: CardDb,
    private val cardApi: CardApi,
    private val region: String
) : RemoteMediator<Int, Card>() {
    private val postDao: CardDao = db.cards()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Card>
    ): MediatorResult {
        try {
            // Get the closest item from PagingState that we want to load data around.
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> null
            }

            val data = cardApi.getForManaCost(
                region = region,
                manacost = when (loadType) {
                    LoadType.REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                }
            ).data

            val items = data.children.map { it.data }

            db.withTransaction {
                postDao.insertAll(items)
            }

            return MediatorResult.Success(endOfPaginationReached = items.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}