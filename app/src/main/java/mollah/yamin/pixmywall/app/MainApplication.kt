package mollah.yamin.pixmywall.app

import android.app.Application
import androidx.databinding.DataBindingUtil
import mollah.yamin.pixmywall.binding.AppDataBindingComponent

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DataBindingUtil.setDefaultComponent(AppDataBindingComponent())
    }
}