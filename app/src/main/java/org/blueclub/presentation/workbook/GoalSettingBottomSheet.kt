package org.blueclub.presentation.workbook

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.blueclub.R
import org.blueclub.databinding.DialogGoalSettingBinding
import org.blueclub.presentation.base.BindingBottomSheetDialogFragment
import org.blueclub.util.UiState
import java.text.DecimalFormat

class GoalSettingBottomSheet :
    BindingBottomSheetDialogFragment<DialogGoalSettingBinding>(R.layout.dialog_goal_setting) {
    private val viewModel: WorkbookViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initLayout()
        collectData()
    }

    private fun initLayout() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.btnGoalSettingFinished.setOnClickListener {
            viewModel.uploadMonthlyGoal()
        }
        val decimalFormat = DecimalFormat("#,###")
        var result = ""
        binding.etGoalSetting.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(txt: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!TextUtils.isEmpty(txt!!.toString()) && txt.toString() != result) {
                    result =
                        decimalFormat.format(txt.toString().replace(",", "").toDouble())
                    binding.etGoalSetting.setText(result)
                    binding.etGoalSetting.setSelection(result.length)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    private fun collectData(){
        viewModel.goalSettingUiState.flowWithLifecycle(lifecycle).onEach {
            when(it){
                is UiState.Success -> {
                    dismiss()
                }
                else -> {}
            }
        }.launchIn(lifecycleScope)
    }
}