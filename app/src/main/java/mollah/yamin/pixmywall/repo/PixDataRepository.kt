package mollah.yamin.pixmywall.repo

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import mollah.yamin.pixmywall.api.ApiService
import mollah.yamin.pixmywall.db.AppDatabase
import mollah.yamin.pixmywall.db.dao.PixDataDao
import mollah.yamin.pixmywall.db.dao.PixDataRemoteKeysDao
import mollah.yamin.pixmywall.models.PixData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository class that works with local and remote data sources.
 */
@Singleton
class PixDataRepository @Inject constructor(
    private val pixDataRemoteRepo: PixDataRemoteRepository,
    private val pixDataDao: PixDataDao,
    private val pixDataRemoteKeysDao: PixDataRemoteKeysDao,
    private val db: AppDatabase
) {

    /**
     * Search repositories whose names match the query, exposed as a stream of data that will emit
     * every time we get more data from the network.
     */
    fun getSearchResultStream(query: String): Flow<PagingData<PixData>> {
        Log.d("GithubRepository", "New query: $query")

        // appending '%' so we can allow other characters to be before and after the query string
        //val dbQuery = query.trim().replace(' ', '%')
        val pagingSourceFactory = { pixDataDao.pixDataBySearchQuery(query) }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = PixDataRemoteMediator(
                pixDataRemoteRepo, pixDataDao, pixDataRemoteKeysDao, db, query
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 40
    }
}
