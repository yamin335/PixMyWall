package mollah.yamin.pixmywall.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mollah.yamin.pixmywall.api.ApiService
import mollah.yamin.pixmywall.models.PixDataResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PixDataRemoteRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getPixDataBySearchQuery(
        queryString: String,
        pageIndex: Int,
        pageSize: Int
    ): PixDataResponse {
        return withContext(Dispatchers.IO) {
            apiService.getPixDataBySearchQuery(
                queryString = queryString,
                pageIndex = pageIndex,
                pageSize = pageSize
            )
        }
    }
}