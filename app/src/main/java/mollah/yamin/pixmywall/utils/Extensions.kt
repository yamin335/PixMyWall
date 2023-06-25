package mollah.yamin.pixmywall.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import kotlin.math.roundToInt

fun Activity.updateStatusBarBackgroundColor(colorResId: Int) {
    try {
        window.statusBarColor = getColor(colorResId)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Int.dpToPx(mContext: Context): Float {
    val rsc = mContext.resources
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), rsc.displayMetrics)
}

fun String.toColor(): Int = Color.parseColor(this)

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    if (currentFocus == null) {
        hideKeyboard(View(this))
    } else {
        hideKeyboard(currentFocus as View)
    }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.applicationWindowToken, 0)
}