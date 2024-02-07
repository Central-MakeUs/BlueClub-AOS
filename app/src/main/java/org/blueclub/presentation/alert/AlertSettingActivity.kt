package org.blueclub.presentation.alert

import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.blueclub.R
import org.blueclub.databinding.ActivityAlertSettingBinding
import org.blueclub.presentation.base.BindingActivity

@AndroidEntryPoint
class AlertSettingActivity :
    BindingActivity<ActivityAlertSettingBinding>(R.layout.activity_alert_setting) {
    private val viewModel: AlertSettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initLayout()
    }

    private fun initLayout() {
        // TODO 유저가 선택한 값 가져오는 로직
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.switchPushAlert.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setAgreement(binding.switchMarketing.isChecked, isChecked)
        }
        binding.switchMarketing.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setAgreement(isChecked, binding.switchPushAlert.isChecked)
        }
    }
}