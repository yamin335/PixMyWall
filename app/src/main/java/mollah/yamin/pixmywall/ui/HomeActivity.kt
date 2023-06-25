package mollah.yamin.pixmywall.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import dagger.hilt.android.AndroidEntryPoint
import mollah.yamin.pixmywall.R
import mollah.yamin.pixmywall.databinding.ActivityHomeBinding
import mollah.yamin.pixmywall.ui.base.NavigationHost
import mollah.yamin.pixmywall.utils.updateStatusBarBackgroundColor

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), NavigationHost {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateStatusBarBackgroundColor(R.color.white)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }
}