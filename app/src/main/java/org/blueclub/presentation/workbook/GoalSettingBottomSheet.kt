package org.blueclub.presentation.workbook

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import org.blueclub.R
import org.blueclub.databinding.DialogGoalSettingBinding
import org.blueclub.presentation.base.BindingBottomSheetDialogFragment

class GoalSettingBottomSheet :
    BindingBottomSheetDialogFragment<DialogGoalSettingBinding>(R.layout.dialog_goal_setting) {
    private val viewModel: WorkbookViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initLayout()
    }

    private fun initLayout() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.btnGoalSettingFinished.setOnClickListener {
            dismiss()
        }
    }
}