package org.blueclub.presentation.auth.setting

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.blueclub.R
import org.blueclub.databinding.ActivityAuthSettingBinding
import org.blueclub.presentation.auth.login.LoginActivity
import org.blueclub.presentation.base.BindingActivity
import org.blueclub.presentation.type.AuthSettingPageViewType

@AndroidEntryPoint
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
        onBackPressedDispatcher.addCallback(this) {
            if(binding.vpAuthsetting.currentItem > 0)
                binding.vpAuthsetting.currentItem--
            else
                moveToLogin()

        }
        binding.vpAuthsetting.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position != AuthSettingPageViewType.GOAL.ordinal){
                    viewModel.isGoalSettingFinished(false)
                }
                viewModel.setCurrentPage(position)
            }
        })
    }

    private fun collectData() {
        viewModel.selectedJobType.flowWithLifecycle(lifecycle).onEach { data ->
            if(binding.vpAuthsetting.currentItem == 0){
                val type = data.entries.find { it.value } ?: return@onEach
                binding.vpAuthsetting.currentItem++
                viewModel.setChosenJobType(type.key)
            }
        }.launchIn(lifecycleScope)
        viewModel.goalSettingFinished.flowWithLifecycle(lifecycle).onEach {
            if (it)
                binding.vpAuthsetting.currentItem++
        }.launchIn(lifecycleScope)

    }
    private fun moveToLogin(){
        Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }.also {
            viewModel.logout()
            startActivity(it)
            finish()
        }
    }
}