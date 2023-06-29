package mollah.yamin.pixmywall.app

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.HiltAndroidApp
import mollah.yamin.pixmywall.api.Api
import mollah.yamin.pixmywall.binding.AppDataBindingComponent

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DataBindingUtil.setDefaultComponent(AppDataBindingComponent())

        val appInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getApplicationInfo(
                packageName,
                PackageManager.ApplicationInfoFlags.of(0)
            )
        } else {

            packageManager.getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        }
        Api.API_KEY = appInfo.metaData["apiKey"].toString()
    }
}