package mollah.yamin.pixmywall.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mollah.yamin.pixmywall.db.AppDatabase
import mollah.yamin.pixmywall.db.dao.PixDataCacheDao
import mollah.yamin.pixmywall.db.dao.PixDataDao
import mollah.yamin.pixmywall.db.dao.PixDataRemoteKeysDao
import javax.inject.Singleton

/**
 * Module to tell Hilt how to provide instances of types that cannot be constructor-injected.
 *
 * As these types are scoped to the application lifecycle using @Singleton, they're installed
 * in Hilt's ApplicationComponent.
 */
@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getInstance(appContext)
    }

    @Singleton
    @Provides
    fun providePixDataDao(db: AppDatabase): PixDataDao {
        return db.pixDataDao()
    }

    @Singleton
    @Provides
    fun providePixDataRemoteKeysDao(db: AppDatabase): PixDataRemoteKeysDao {
        return db.pixDataRemoteKeysDao()
    }

    @Singleton
    @Provides
    fun providePixDataCacheDao(db: AppDatabase): PixDataCacheDao {
        return db.pixDataCacheDao()
    }
}
