package vancore.playground.hearthstone.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import vancore.playground.hearthstone.data.model.Card

/**
 * Common interface shared by the different repository implementations.
 * Note: this only exists for sample purposes - typically an app would implement a repo once, either
 * network+db, or network-only
 */
interface CardRepository {
    fun cards(region: String, pageSize: Int): Flow<PagingData<Card>>
}