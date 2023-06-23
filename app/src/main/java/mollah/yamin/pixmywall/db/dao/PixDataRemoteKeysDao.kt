package mollah.yamin.pixmywall.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mollah.yamin.pixmywall.models.PixDataRemoteKey

@Dao
interface PixDataRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<PixDataRemoteKey>)

    @Query("SELECT * FROM pix_data_remote_keys WHERE id = :pixDataId")
    suspend fun remoteKeysByPixDataId(pixDataId: Long): PixDataRemoteKey?
}
