package mollah.yamin.pixmywall.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mollah.yamin.pixmywall.models.PixData

@Dao
interface PixDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<PixData>)

    @Query("SELECT * FROM pix_data WHERE `query` LIKE '%' || :query || '%' ORDER BY page_index")
    fun pixDataBySearchQuery(query: String): PagingSource<Int, PixData>

    @Query("DELETE FROM pix_data")
    suspend fun clearCachedPixData()
}
