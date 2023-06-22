package mollah.yamin.pixmywall.models

data class PixDataResponse(val total: Int, val totalHits: Int, val hits: List<PixData>)

data class PixData(val id: Long, val tags: String, val previewURL: String,
                   val largeImageURL: String, val user: String, val downloads: Int,
                   val likes: Int, val comments: Int)
