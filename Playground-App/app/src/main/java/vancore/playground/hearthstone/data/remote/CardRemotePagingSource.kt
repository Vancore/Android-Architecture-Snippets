package vancore.playground.hearthstone.data.remote

import androidx.paging.PagingSource
import vancore.playground.hearthstone.data.model.Card

//class CardRemotePagingSource (val cardService: CardService) : PagingSource<Int, Card>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Card> {
//        try {
//            val page = params.key ?: 1
//            val response = cardService.getCards()
//
//            return LoadResult.Page(
//                data = response,
//                prevKey = page - 1,
//                nextKey = page + 1
//            )
//        } catch (e: Exception) {
//            return LoadResult.Error(e)
//        }
//    }


//}