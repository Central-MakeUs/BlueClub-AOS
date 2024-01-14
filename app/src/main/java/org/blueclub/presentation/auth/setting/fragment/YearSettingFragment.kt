package org.blueclub.presentation.auth.setting.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.blueclub.R
import org.blueclub.databinding.FragmentYearSettingBinding
import org.blueclub.presentation.auth.setting.AuthSettingViewModel
import org.blueclub.presentation.base.BindingFragment

class YearSettingFragment :
    BindingFragment<FragmentYearSettingBinding>(R.layout.fragment_year_setting) {
    private val viewModel: AuthSettingViewModel by activityViewModels()
    private var dialog = YearSettingBottomSheetDialog()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initLayout()
        collectData()
    }

    private fun initLayout() {
        binding.layoutYear.setOnClickListener {
            dialog.show(parentFragmentManager, "yearSetting")
        }
        binding.btnNext.setOnClickListener {
            viewModel.setYearSelected(true)
        }
    }

    private fun collectData(){
        viewModel.chosenJobType.flowWithLifecycle(lifecycle).onEach {
            dialog = YearSettingBottomSheetDialog()
        }.launchIn(lifecycleScope)
    }
}