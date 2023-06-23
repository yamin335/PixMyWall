package mollah.yamin.pixmywall.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Keep
data class PixDataResponse(val total: Int, val totalHits: Int, val hits: List<PixData>)

/*
* It's best practice to define a string constant if it is used in multiple places
* to avoid unexpected errors caused by changing the value in one use case.
* */
const val COLUMN_ID = "id"
@Keep
@Entity(tableName = "pix_data",
    indices = [Index(value = ["query"], unique = false)])
data class PixData(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: Long,
    @ColumnInfo(name = "tags")
    val tags: String?,
    @ColumnInfo(name = "preview_url")
    val previewURL: String?,
    @ColumnInfo(name = "large_image_url")
    val largeImageURL: String?,
    @ColumnInfo(name = "user_name")
    val user: String?,
    @ColumnInfo(name = "downloads")
    val downloads: Int,
    @ColumnInfo(name = "likes")
    val likes: Int,
    @ColumnInfo(name = "comments")
    val comments: Int,
    @ColumnInfo(name = "query")
    val query: String?
)
