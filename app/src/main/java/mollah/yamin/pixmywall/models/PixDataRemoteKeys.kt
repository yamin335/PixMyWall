package mollah.yamin.pixmywall.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Keep
//@Entity(tableName = "pix_data_remote_keys", foreignKeys = [
//    ForeignKey(
//        entity = PixData::class,
//        parentColumns = [COLUMN_ID],
//        childColumns = [COLUMN_ID],
//        onDelete = CASCADE,
//        onUpdate = CASCADE,
//        deferred = false
//    )], indices = [Index(value = [COLUMN_ID], unique = true)])
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
