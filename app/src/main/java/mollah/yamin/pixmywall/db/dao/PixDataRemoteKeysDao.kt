package mollah.yamin.pixmywall.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mollah.yamin.pixmywall.models.PixDataRemoteKey

@Dao
interface PixDataRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAll(remoteKey: List<PixDataRemoteKey>)

    @Query("SELECT * FROM pix_data_remote_keys WHERE id = :pixDataId")
    suspend fun remoteKeysByPixDataId(pixDataId: Long): PixDataRemoteKey?

    @Query("DELETE FROM pix_data_remote_keys")
    suspend fun clearRemoteKeys()

}
