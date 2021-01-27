package vancore.playground.hearthstone.data.remote

import android.util.Log
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import vancore.playground.hearthstone.data.model.Card

/**
 * API communication setup
 */
interface CardApi {

    @GET("/r/{subreddit}/hot.json")
    suspend fun getForManaCost(
        @Path("region") region: String,
        @Query("manaCost") manacost: Int
    ): ListingResponse

    class ListingResponse(val data: ListingData)

    class ListingData(
        val children: List<CardResponse>,
        val after: String?,
        val before: String?
    )

    data class CardResponse(val data: Card)

    companion object {
        private const val BASE_URL = "https://www.reddit.com/"
        fun create(): CardApi {
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Log.d("API", it) })
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(HttpUrl.parse(BASE_URL)!!)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CardApi::class.java)
        }
    }
}