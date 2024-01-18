package org.blueclub.presentation.auth.setting.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import org.blueclub.R
import org.blueclub.databinding.FragmentJobSettingBinding
import org.blueclub.presentation.auth.setting.AuthSettingViewModel
import org.blueclub.presentation.base.BindingFragment

class JobSettingFragment :
    BindingFragment<FragmentJobSettingBinding>(R.layout.fragment_job_setting) {
    private val viewModel: AuthSettingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

    }
}