package org.blueclub.presentation.auth.setting

import android.os.Bundle
import androidx.activity.viewModels
import org.blueclub.R
import org.blueclub.databinding.ActivityAuthSettingBinding
import org.blueclub.presentation.base.BindingActivity

class AuthSettingActivity : BindingActivity<ActivityAuthSettingBinding>(R.layout.activity_auth_setting) {
    private val viewModel : AuthSettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initLayout()

    }

    private fun initLayout(){
        binding.vpAuthsetting.adapter = AuthSettingAdapter(this)
    }
}