package mollah.yamin.pixmywall.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import mollah.yamin.pixmywall.R
import mollah.yamin.pixmywall.utils.updateStatusBarBackgroundColor

class GalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateStatusBarBackgroundColor(R.color.white)
        setContentView(R.layout.activity_gallery)
    }
}