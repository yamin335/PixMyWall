package mollah.yamin.pixmywall.api

import mollah.yamin.pixmywall.BuildConfig
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

    /*
    * Do not place secret keys like that in production.
    * Use app preferences to store secret keys making custom
    * session manager. */
    var API_KEY = BuildConfig.API_KEY
}

object ApiEndPoint {
    const val IMAGE_QUERY = "$API_REPO/"
}
