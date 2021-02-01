package vancore.playground.hearthstone.data.remote

import android.util.Log
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import vancore.playground.hearthstone.authentication.LoginResponse
import vancore.playground.hearthstone.data.model.Card

/**
 * API communication setup
 */
interface CardApi {

    @POST("https://us.battle.net/oauth/token")
    @FormUrlEncoded
    fun login(@Field("grant_type") grant_type: String,
                      @Field("client_secret") client_secret: String,
                      @Field("client_id") client_id: String
    ): Call<LoginResponse>

    //{API path}
    @GET("{region}.api.blizzard.com/")
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

        //grant_type:client_credentials
        //curl -u {client_id}:{client_secret} -d grant_type=client_credentials https://us.battle.net/oauth/token
        //client secret: LEWHj96ZC2dVT94m8jMSDHKAvrYRMSYK
        //client id: 83d65959a7484ec18f48a60f2f9f4c00
        private const val BASE_URL = "https://www.reddit.com/"
        fun create(): CardApi {
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Log.d("API", it) })
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL.toHttpUrlOrNull()!!)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CardApi::class.java)
        }
    }
}