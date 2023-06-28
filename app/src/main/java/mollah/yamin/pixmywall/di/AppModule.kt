package mollah.yamin.pixmywall.di

import android.Manifest
import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import androidx.annotation.RequiresPermission
import coil.ImageLoader
import coil.disk.DiskCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mollah.yamin.pixmywall.coil.PixDataLargePhotoMapper
import mollah.yamin.pixmywall.utils.NetworkUtils
import javax.inject.Singleton

/**
 * Module to tell Hilt how to provide instances of types that cannot be constructor-injected.
 *
 * As these types are scoped to the application lifecycle using @Singleton, they're installed
 * in Hilt's ApplicationComponent.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesWifiManager(@ApplicationContext context: Context): WifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @Singleton
    @Provides
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun providesConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

    @Singleton
    @Provides
    fun providesClipboardManager(@ApplicationContext context: Context): ClipboardManager =
        context.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE)
                as ClipboardManager

    @Singleton
    @Provides
    fun provideNetworkUtils(connectivityManager: ConnectivityManager): NetworkUtils =
        NetworkUtils(connectivityManager = connectivityManager)

    @Singleton
    @Provides
    @LargeImageLoader
    fun provideLargeImageLoader(@ApplicationContext context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                add(PixDataLargePhotoMapper())
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("large_image_cache"))
                    .maxSizePercent(0.5)
                    .build()
            }
            .build()
    }
}
