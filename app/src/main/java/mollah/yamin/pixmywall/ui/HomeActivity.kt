package mollah.yamin.pixmywall.ui

import android.os.Bundle
import androidx.core.os.BuildCompat
import dagger.hilt.android.AndroidEntryPoint
import mollah.yamin.pixmywall.R
import mollah.yamin.pixmywall.databinding.ActivityHomeBinding
import mollah.yamin.pixmywall.ui.base.BaseActivity
import mollah.yamin.pixmywall.utils.updateStatusBarBackgroundColor

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