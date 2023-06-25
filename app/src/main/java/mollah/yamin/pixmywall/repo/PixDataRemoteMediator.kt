package mollah.yamin.pixmywall.repo

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import mollah.yamin.pixmywall.db.AppDatabase
import mollah.yamin.pixmywall.db.dao.PixDataDao
import mollah.yamin.pixmywall.db.dao.PixDataRemoteKeysDao
import mollah.yamin.pixmywall.models.PixData
import mollah.yamin.pixmywall.models.PixDataRemoteKey
import okio.IOException
import retrofit2.HttpException

private const val PIX_DATA_START_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class PixDataRemoteMediator(
    private val pixDataRemoteRepo: PixDataRemoteRepository,
    private val pixDataDao: PixDataDao,
    private val pixDataRemoteKeysDao: PixDataRemoteKeysDao,
    private val db: AppDatabase,
    private val query: String
) : RemoteMediator<Int, PixData>() {

    override suspend fun initialize(): InitializeAction {
        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, PixData>): MediatorResult {

        val pageIndex = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: PIX_DATA_START_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val apiResponse = pixDataRemoteRepo.getPixDataBySearchQuery(query, pageIndex, state.config.pageSize)

            val pixDataList = apiResponse.hits ?: ArrayList()
            val endOfPaginationReached = pixDataList.isEmpty()
            db.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    pixDataRemoteKeysDao.clearRemoteKeys()
                    pixDataDao.clearCachedPixData()
                }
                val prevKey = if (pageIndex == PIX_DATA_START_PAGE_INDEX) null else pageIndex - 1
                val nextKey = if (endOfPaginationReached) null else pageIndex + 1
                val newList: ArrayList<PixData> = ArrayList()
                val keys: ArrayList<PixDataRemoteKey> = ArrayList()
                for (pixData in pixDataList) {
                    val temp = PixData(
                        pixData.id, pixData.tags, pixData.previewURL,
                        pixData.largeImageURL, pixData.user, pixData.downloads,
                        pixData.likes, pixData.comments, query, pageIndex
                    )
                    newList.add(temp)
                    keys.add(PixDataRemoteKey(pixDataId = pixData.id, prevKey = prevKey, nextKey = nextKey))
                }
                pixDataRemoteKeysDao.insertAll(keys)
                pixDataDao.insertAll(newList)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PixData>): PixDataRemoteKey? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { pixData ->
                // Get the remote keys of the last item retrieved
                Log.d("DEBUG_LOG:", "Last item id: ${pixData.id}")
                pixDataRemoteKeysDao.remoteKeysByPixDataId(pixData.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PixData>): PixDataRemoteKey? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { pixData ->
                // Get the remote keys of the first items retrieved
                Log.d("DEBUG_LOG:", "First item id: ${pixData.id}")
                pixDataRemoteKeysDao.remoteKeysByPixDataId(pixData.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PixData>
    ): PixDataRemoteKey? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                pixDataRemoteKeysDao.remoteKeysByPixDataId(repoId)
            }
        }
    }
}