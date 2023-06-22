package mollah.yamin.pixmywall.utils

import android.app.Activity
import android.graphics.Color

fun Activity.updateStatusBarBackgroundColor(colorResId: Int) {
    try {
        window.statusBarColor = getColor(colorResId)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun String.toColor(): Int = Color.parseColor(this)