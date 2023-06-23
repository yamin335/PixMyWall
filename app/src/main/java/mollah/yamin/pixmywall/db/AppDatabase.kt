package mollah.yamin.pixmywall.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mollah.yamin.pixmywall.BuildConfig
import mollah.yamin.pixmywall.db.dao.PixDataDao
import mollah.yamin.pixmywall.db.dao.PixDataRemoteKeysDao
import mollah.yamin.pixmywall.models.PixData
import mollah.yamin.pixmywall.models.PixDataRemoteKey

private const val DATABASE = "PixWall.db"
private const val DATABASE_VERSION = 1

/**
 * PixWall database.
 */
@Database(
    entities = [
        PixData::class,
        PixDataRemoteKey::class
    ],
    version = DATABASE_VERSION,
    exportSchema = true
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun pixDataDao(): PixDataDao
    abstract fun pixDataRemoteKeysDao(): PixDataRemoteKeysDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(appContext: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(
                appContext
            ).also { INSTANCE = it }
        }

        private fun buildDatabase(appContext: Context) = if (BuildConfig.DEBUG) {
            Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE)
                .fallbackToDestructiveMigration()
                .build()
        } else {
            Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE)
                .build()
        }
    }
}