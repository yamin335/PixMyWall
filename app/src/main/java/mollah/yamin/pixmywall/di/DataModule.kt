package mollah.yamin.pixmywall.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mollah.yamin.pixmywall.api.ApiService
import mollah.yamin.pixmywall.db.AppDatabase
import mollah.yamin.pixmywall.db.dao.PixDataDao
import mollah.yamin.pixmywall.db.dao.PixDataRemoteKeysDao
import mollah.yamin.pixmywall.repo.PixDataRemoteRepository
import mollah.yamin.pixmywall.repo.PixDataRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun providePixDataRemoteRepository(
        apiService: ApiService
    ): PixDataRemoteRepository {
        return PixDataRemoteRepository(apiService)
    }

    @Singleton
    @Provides
    fun providePixDataRepository(
        pixDataRemoteRepo: PixDataRemoteRepository,
        pixDataDao: PixDataDao,
        pixDataRemoteKeysDao: PixDataRemoteKeysDao,
        database: AppDatabase
    ): PixDataRepository {
        return PixDataRepository(
            pixDataRemoteRepo, pixDataDao,
            pixDataRemoteKeysDao, database
        )
    }
}