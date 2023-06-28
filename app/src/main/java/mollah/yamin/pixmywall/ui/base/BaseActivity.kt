package mollah.yamin.pixmywall.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.AndroidEntryPoint
import mollah.yamin.pixmywall.utils.NetworkObserver
import mollah.yamin.pixmywall.utils.NetworkUtils
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity: AppCompatActivity(), NavigationHost {
    @Inject
    lateinit var networkUtils: NetworkUtils

    var networkObserver: NetworkObserver? = null

    var isConnected: MutableLiveData<Boolean> = MutableLiveData()

    var initialConnectionStatus: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialConnectionStatus = networkUtils.isNetworkConnected()
        isConnected.postValue(networkUtils.isNetworkConnected())
        networkUtils.observeNetwork(object : NetworkObserver {
            override fun connected() {
                networkObserver?.connected()
                isConnected.postValue(true)
                initialConnectionStatus = true
            }

            override fun disconnected() {
                networkObserver?.disconnected()
                isConnected.postValue(true)
                initialConnectionStatus = false
            }
        })
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }
}