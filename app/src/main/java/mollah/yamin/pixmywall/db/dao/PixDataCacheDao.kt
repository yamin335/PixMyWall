package mollah.yamin.pixmywall.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import mollah.yamin.pixmywall.models.PixDataCache

@Dao
interface PixDataCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<PixDataCache>)

    @Query("SELECT * FROM pix_data_cache")
    fun getCurrentCachedData(): Flow<List<PixDataCache>>

    @Query("DELETE FROM pix_data_cache")
    suspend fun clearCachedPixData()

    @Transaction
    suspend fun updatePixDataCache(list: List<PixDataCache>) {
        clearCachedPixData()
        insertAll(list)
    }
}
