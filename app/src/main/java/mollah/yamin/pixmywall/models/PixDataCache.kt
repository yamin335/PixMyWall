package mollah.yamin.pixmywall.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "pix_data_cache")
data class PixDataCache(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "preview_url")
    val previewURL: String?,
    @ColumnInfo(name = "large_image_url")
    val largeImageURL: String?,
    @ColumnInfo(name = "user_image_url")
    val userImageURL: String?
)
