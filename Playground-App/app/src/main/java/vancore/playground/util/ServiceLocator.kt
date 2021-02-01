package vancore.playground.util

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.paging.ExperimentalPagingApi
import vancore.playground.hearthstone.data.CardRepository
import vancore.playground.hearthstone.data.local.CardDb
import vancore.playground.hearthstone.data.local.DbCardRepository
import vancore.playground.hearthstone.data.remote.CardApi

/**
 * Super simplified service locator implementation to allow us to replace default implementations
 * for testing.
 */
interface ServiceLocator {

    @ExperimentalPagingApi
    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null
        fun instance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(
                        app = context.applicationContext as Application,
                        useInMemoryDb = false
                    )
                }
                return instance!!
            }
        }

        /**
         * Allows tests to replace the default implementations.
         */
        @VisibleForTesting
        fun swap(locator: ServiceLocator) {
            instance = locator
        }
    }

    fun getRepository(): CardRepository

    fun getCardApi(): CardApi
}

/**
 * default implementation of ServiceLocator that uses production endpoints.
 */
@ExperimentalPagingApi
open class DefaultServiceLocator(val app: Application, val useInMemoryDb: Boolean) :
    ServiceLocator {
    private val db by lazy {
        CardDb.create(app, useInMemoryDb)
    }

    private val api by lazy {
        CardApi.create()
    }

    override fun getRepository(): CardRepository {
        return DbCardRepository(
            db = db,
            cardApi = getCardApi()
        )
    }

    override fun getCardApi(): CardApi = api
}