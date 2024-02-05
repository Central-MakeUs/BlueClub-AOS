package org.blueclub.presentation.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import org.blueclub.R
import org.blueclub.databinding.DialogJobSelectingBinding
import org.blueclub.presentation.base.BindingBottomSheetDialogFragment
import org.blueclub.presentation.type.JobSettingViewType

class JobSelectingBottomSheet :
    BindingBottomSheetDialogFragment<DialogJobSelectingBinding>(R.layout.dialog_job_selecting) {
    private val viewModel: ProfileSettingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        initLayout()
    }

    private fun initLayout() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.tvGolf.setOnClickListener {
            viewModel.setJobType(JobSettingViewType.GOLF)
            dismiss()
        }
        binding.tvRider.setOnClickListener {
            viewModel.setJobType(JobSettingViewType.RIDER)
            dismiss()
        }
        binding.tvDayLabor.setOnClickListener {
            viewModel.setJobType(JobSettingViewType.DAY_LABOR)
            dismiss()
        }
    }
}