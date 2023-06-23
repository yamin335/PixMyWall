/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
//        onUpdate = ForeignKey.NO_ACTION,
//        deferred = false
//    )], indices = [Index(value = [COLUMN_ID], unique = true)])
@Entity(tableName = "pix_data_remote_keys",
    indices = [Index(value = [COLUMN_ID], unique = true)])
data class PixDataRemoteKey(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val pixDataId: Long,
    @ColumnInfo(name = "prev_key")
    val prevKey: Int?,
    @ColumnInfo(name = "next_key")
    val nextKey: Int?
)
