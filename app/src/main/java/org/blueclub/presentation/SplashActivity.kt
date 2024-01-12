package org.blueclub.presentation

import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.blueclub.R
import org.blueclub.databinding.ActivitySplashBinding
import org.blueclub.presentation.auth.login.LoginActivity
import org.blueclub.presentation.base.BindingActivity

@AndroidEntryPoint
class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            delay(1000L)
            moveToLogin()
        }
    }

    private fun moveToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}