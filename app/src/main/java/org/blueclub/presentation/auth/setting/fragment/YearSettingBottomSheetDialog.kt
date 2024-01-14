package org.blueclub.presentation.auth.setting.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import org.blueclub.R
import org.blueclub.databinding.DialogYearSettingBinding
import org.blueclub.presentation.auth.setting.AuthSettingViewModel
import org.blueclub.presentation.base.BindingBottomSheetDialogFragment

class YearSettingBottomSheetDialog :
    BindingBottomSheetDialogFragment<DialogYearSettingBinding>(R.layout.dialog_year_setting) {
    private lateinit var yearSettingAdapter: YearSettingAdapter
    private var yearList = (2024 downTo 1980).map { it to false }.toMutableList()
    private val viewModel: AuthSettingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLayout()
    }

    private fun initLayout() {
        yearSettingAdapter = YearSettingAdapter(
            requireContext(),
            yearList,
            ::dismissDialog
        )
        binding.rvYearSelect.apply {
            itemAnimator = null
            adapter = yearSettingAdapter
        }
        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }

    private fun dismissDialog(selectedIndex: Int) {
        viewModel.setSelectedYear(yearList[selectedIndex].first.toString())
        yearList = yearList.map {
            if (it.first == yearList[selectedIndex].first)
                it.first to true
            else
                it.first to false
        }.toMutableList()
        dismiss()
    }
}