package vancore.playground.hearthstone.data.local

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import vancore.playground.hearthstone.data.CardRepository
import vancore.playground.hearthstone.data.remote.CardApi
import vancore.playground.hearthstone.data.remote.CardRemoteMediator

@ExperimentalPagingApi
class DbCardRepository(val db: CardDb, val cardApi: CardApi) : CardRepository {

    override fun cards(region: String, pageSize: Int) = Pager(
        config = PagingConfig(pageSize),
        remoteMediator = CardRemoteMediator(db, cardApi, region)
    ) {
        db.cards().cardByManacost(5)
    }.flow
}