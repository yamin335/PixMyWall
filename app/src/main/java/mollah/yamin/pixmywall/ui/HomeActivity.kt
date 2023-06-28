package mollah.yamin.pixmywall.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import dagger.hilt.android.AndroidEntryPoint
import mollah.yamin.pixmywall.R
import mollah.yamin.pixmywall.databinding.ActivityHomeBinding
import mollah.yamin.pixmywall.ui.base.NavigationHost
import mollah.yamin.pixmywall.utils.updateStatusBarBackgroundColor
import androidx.activity.addCallback
import androidx.core.os.BuildCompat
import mollah.yamin.pixmywall.ui.base.BaseActivity

@AndroidEntryPoint
class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding

    @androidx.annotation.OptIn(BuildCompat.PrereleaseSdkCheck::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateStatusBarBackgroundColor(R.color.white)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        if (BuildCompat.isAtLeastT()) {
//            onBackInvokedDispatcher.registerOnBackInvokedCallback(
//                OnBackInvokedDispatcher.PRIORITY_DEFAULT
//            ) {
//               // Back button is pressed
//            }
//        } else {
//            onBackPressedDispatcher.addCallback(this) {
//                // Back button is pressed
//            }
//        }
    }
}