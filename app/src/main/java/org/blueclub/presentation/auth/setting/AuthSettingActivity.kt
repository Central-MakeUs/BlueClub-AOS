package org.blueclub.presentation.auth.setting

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.blueclub.R
import org.blueclub.databinding.ActivityAuthSettingBinding
import org.blueclub.presentation.base.BindingActivity

class AuthSettingActivity :
    BindingActivity<ActivityAuthSettingBinding>(R.layout.activity_auth_setting) {
    private val viewModel: AuthSettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initLayout()
        collectData()
    }

    private fun initLayout() {
        binding.vpAuthsetting.adapter = AuthSettingAdapter(this)
        binding.vpAuthsetting.isUserInputEnabled = false
        binding.viewToolbar.ivBack.setOnClickListener {
            binding.vpAuthsetting.currentItem--
        }
    }

    private fun collectData() {
        viewModel.selectedJobType.flowWithLifecycle(lifecycle).onEach { data ->
            val type = data.entries.find { it.value } ?: return@onEach
            binding.vpAuthsetting.currentItem++
            viewModel.setChosenJobType(type.key)
            viewModel.setSelectedYear(null)
        }.launchIn(lifecycleScope)
        viewModel.yearSelectFinished.flowWithLifecycle(lifecycle).onEach {
            if(it)
                binding.vpAuthsetting.currentItem++
        }.launchIn(lifecycleScope)
    }
}