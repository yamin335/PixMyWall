package mollah.yamin.pixmywall.ui.base

import android.content.Context
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar

/**
 * To be implemented by components that host top-level navigation destinations.
 */
interface NavigationHost {

    /** Called by MainNavigationFragment to setup it's toolbar with the navigation controller. */
    fun registerToolbarWithNavigation(toolbar: Toolbar)
}

abstract class BaseFragment: Fragment() {
    var navHost: NavigationHost? = null

    lateinit var viewDataBinding: ViewDataBinding

    val navController: NavController
        get() = findNavController()

    val mContext: Context
        get() = requireContext()

    val mActivity: FragmentActivity
        get() = requireActivity()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NavigationHost) {
            navHost = context
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
}