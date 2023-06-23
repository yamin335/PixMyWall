package mollah.yamin.pixmywall.api

import mollah.yamin.pixmywall.api.Api.API_KEY
import mollah.yamin.pixmywall.api.Api.PHOTO_IMAGE_TYPE
import mollah.yamin.pixmywall.models.PixDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * REST API access points
 */
interface ApiService {
    @GET(ApiEndPoint.IMAGE_QUERY)
    suspend fun getPixDataBySearchQuery(
        @Query("key") key: String = API_KEY,
        @Query("q") queryString: String,
        @Query("image_type") imageType: String = PHOTO_IMAGE_TYPE,
        @Query("page") pageIndex: Int,
        @Query("per_page") pageSize: Int
    ): Response<PixDataResponse>
}
