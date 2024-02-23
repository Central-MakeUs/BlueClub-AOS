package org.blueclub.presentation

import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.blueclub.R
import org.blueclub.data.datasource.BCDataSource
import org.blueclub.databinding.ActivitySplashBinding
import org.blueclub.presentation.auth.login.LoginActivity
import org.blueclub.presentation.auth.setting.AuthSettingActivity
import org.blueclub.presentation.base.BindingActivity
import org.blueclub.presentation.home.MainActivity

@AndroidEntryPoint
class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            delay(2000L)
            moveToNext()
        }
    }

    private fun moveToNext() {
        val storage = BCDataSource(this)
        val nextScreen = if(storage.isLogin) {
            if(storage.job == null)
                AuthSettingActivity::class.java
            else
                MainActivity::class.java
        } else LoginActivity::class.java
        startActivity(Intent(this, nextScreen))
        finish()
    }
}