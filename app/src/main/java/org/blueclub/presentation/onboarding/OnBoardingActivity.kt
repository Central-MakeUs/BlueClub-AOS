package org.blueclub.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.blueclub.R
import org.blueclub.databinding.ActivityOnboardingBinding
import org.blueclub.presentation.auth.AuthSettingActivity
import org.blueclub.presentation.base.BindingActivity

@AndroidEntryPoint
class OnBoardingActivity :
    BindingActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {
    private val viewModel: OnBoardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initLayout()
        addListeners()
    }

    private fun initLayout() {
        with(binding) {
            vpOnboarding.adapter = OnBoardingAdapter(this@OnBoardingActivity)
            vpOnboarding.registerOnPageChangeCallback(getPageChangeCallback())
            TabLayoutMediator(indicator, vpOnboarding) { _, _ -> }.attach()
        }
    }

    private fun addListeners() {
        binding.btnMoveToSign.setOnClickListener {
            moveToSign()
        }
    }

    private fun getPageChangeCallback() = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            viewModel.setOnboardingType(position)
        }
    }

    private fun moveToSign(){
        startActivity(Intent(this, AuthSettingActivity::class.java))
        finish()
    }
}