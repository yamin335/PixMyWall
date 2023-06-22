package mollah.yamin.pixmywall.api

import mollah.yamin.pixmywall.api.Api.API_REPO

object Api {
    /*
    * There might be different cases like "Test Server" and "Production Server"
    * that's why API URLs are constructed in parts to reduce code redundancy. */

    private const val PROTOCOL = "https"
    private const val API_ROOT = "pixabay.com"
    const val API_ROOT_URL = "$PROTOCOL://$API_ROOT/"

    const val API_REPO = "api"
    const val PHOTO_IMAGE_TYPE = "photo"

    const val ContentType = "Content-Type: application/json"
}

object ApiEndPoint {
    const val IMAGE_QUERY = "$API_REPO/"
}

object ResponseCodes {
    const val CODE_SUCCESS = 200
    const val CODE_LIMIT_EXCEEDED = 429
    const val CODE_UNAUTHORIZED = 400
}
