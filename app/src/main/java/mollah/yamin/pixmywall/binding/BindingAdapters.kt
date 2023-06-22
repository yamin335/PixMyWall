package mollah.yamin.pixmywall.binding

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * Binding adapters for data binding
 */

class BindingAdapters {
    @BindingAdapter("showHideBottomNavBar")
    fun showHideBottomNavBar(view: View, visible: Boolean?) {
        view.visibility = if (visible == true) View.VISIBLE else View.GONE
    }
}



