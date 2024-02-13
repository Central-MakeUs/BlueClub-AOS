package org.blueclub.presentation.daily

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import org.blueclub.R
import org.blueclub.databinding.DialogWorkTypeSelectingBinding
import org.blueclub.presentation.base.BindingBottomSheetDialogFragment
import org.blueclub.presentation.type.DailyWorkType

@AndroidEntryPoint
class WorkTypeSettingBottomSheet:BindingBottomSheetDialogFragment<DialogWorkTypeSelectingBinding>(R.layout.dialog_work_type_selecting) {
    private val viewModel: DailyWorkDetailViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initLayout()
    }

    private fun initLayout(){
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.tvWork.setOnClickListener {
            viewModel.setWorkType(DailyWorkType.WORK)
            dismiss()
        }
        binding.tvEarly.setOnClickListener {
            viewModel.setWorkType(DailyWorkType.EARLY)
            dismiss()
        }
        binding.tvRest.setOnClickListener {
            viewModel.setWorkType(DailyWorkType.REST)
            dismiss()
        }
    }
}