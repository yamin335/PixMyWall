package mollah.yamin.pixmywall.utils

import android.Manifest
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import javax.inject.Inject
import javax.inject.Singleton

interface NetworkObserver {
    fun connected()
    fun disconnected()
}

@Singleton
class NetworkUtils @Inject constructor(
    private val connectivityManager: ConnectivityManager
) {
    fun isNetworkConnected(): Boolean {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        return checkValidInternet(activeNetwork)
    }

    private fun checkValidInternet(network: Network): Boolean {
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    @RequiresPermission(Manifest.permission.CHANGE_NETWORK_STATE)
    fun observeNetwork(callback: NetworkObserver) {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            // network is available for use
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                callback.connected()
            }

            // lost network connection
            override fun onLost(network: Network) {
                super.onLost(network)
                callback.disconnected()
            }
        }

        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }
}