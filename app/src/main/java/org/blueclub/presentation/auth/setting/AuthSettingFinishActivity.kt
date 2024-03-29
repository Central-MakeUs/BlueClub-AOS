package org.blueclub.presentation.auth.setting

import android.content.Intent
import android.os.Bundle
import org.blueclub.R
import org.blueclub.databinding.ActivityAuthSettingFinishBinding
import org.blueclub.presentation.base.BindingActivity
import org.blueclub.presentation.home.MainActivity

class AuthSettingFinishActivity :
    BindingActivity<ActivityAuthSettingFinishBinding>(R.layout.activity_auth_setting_finish) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initLayout()
    }

    private fun initLayout() {
        binding.btnStartNow.setOnClickListener {
            moveToMain()
        }
    }

    private fun moveToMain() {
        Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }.also {
            startActivity(it)
            finish()
        }
    }
}