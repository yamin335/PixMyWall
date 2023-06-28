package mollah.yamin.pixmywall.coil

import coil.map.Mapper
import coil.request.Options
import mollah.yamin.pixmywall.models.PixData

class PixDataLargePhotoMapper : Mapper<PixData, String> {
    override fun map(data: PixData, options: Options) = data.largeImageURL
}