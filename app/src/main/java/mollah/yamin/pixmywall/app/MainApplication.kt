package mollah.yamin.pixmywall.app

import android.app.Application
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.HiltAndroidApp
import mollah.yamin.pixmywall.binding.AppDataBindingComponent
@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DataBindingUtil.setDefaultComponent(AppDataBindingComponent())
    }
}