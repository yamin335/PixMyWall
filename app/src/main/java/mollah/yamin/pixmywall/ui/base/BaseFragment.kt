package mollah.yamin.pixmywall.ui.base

import android.content.Context
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import mollah.yamin.pixmywall.ui.HomeActivity
import mollah.yamin.pixmywall.utils.NetworkObserver

/**
 * To be implemented by components that host top-level navigation destinations.
 */
interface NavigationHost {

    /** To setup toolbar with the navigation controller. */
    fun registerToolbarWithNavigation(toolbar: Toolbar)
}

@AndroidEntryPoint
abstract class BaseFragment: Fragment(), NetworkObserver {

    private var navHost: NavigationHost? = null

    private val navController: NavController
        get() = findNavController()

    val mContext: Context
        get() = requireContext()

    val mActivity: FragmentActivity
        get() = requireActivity()

    var isConnected: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NavigationHost) {
            navHost = context
        }
    }

    override fun onResume() {
        super.onResume()
        if (mActivity is HomeActivity) {
            val parentActivity = (mActivity as HomeActivity)
            parentActivity.networkObserver = this
            isConnected.postValue(parentActivity.initialConnectionStatus)
        }
    }

    override fun onDetach() {
        super.onDetach()
        navHost = null
    }

    fun registerToolbar(toolbar: MaterialToolbar) {
        val host = navHost ?: return
        toolbar.apply {
            host.registerToolbarWithNavigation(this)
        }
    }

    fun navigateTo(direction: NavDirections) {
        try {
            navController.navigate(direction)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun popBackStack() = navController.popBackStack()

    override fun connected() {
        isConnected.postValue(true)
    }

    override fun disconnected() {
        isConnected.postValue(false)
    }
}