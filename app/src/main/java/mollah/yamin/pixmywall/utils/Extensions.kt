package mollah.yamin.pixmywall.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
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