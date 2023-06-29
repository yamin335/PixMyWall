package mollah.yamin.pixmywall.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import mollah.yamin.pixmywall.R
import mollah.yamin.pixmywall.app.MainApplication

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * This [LauncherActivity] does not have any UI because
         * it is used only for routing purpose, If there is any task to
         * complete initially then it can be started from here or from the
         * [MainApplication] according to the type of the tasks.*/

        val intent = Intent(this@LauncherActivity, HomeActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
}