package mollah.yamin.pixmywall.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "pix_data_remote_keys")
data class PixDataRemoteKey(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val pixDataId: Long,
    @ColumnInfo(name = "prev_key")
    val prevKey: Int?,
    @ColumnInfo(name = "next_key")
    val nextKey: Int?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
