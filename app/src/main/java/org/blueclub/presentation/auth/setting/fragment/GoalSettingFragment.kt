package org.blueclub.presentation.auth.setting.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.activityViewModels
import org.blueclub.R
import org.blueclub.databinding.FragmentGoalSettingBinding
import org.blueclub.presentation.auth.setting.AuthSettingViewModel
import org.blueclub.presentation.base.BindingFragment
import java.text.DecimalFormat

class GoalSettingFragment :
    BindingFragment<FragmentGoalSettingBinding>(R.layout.fragment_goal_setting) {
    private val viewModel: AuthSettingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initLayout()
    }

    private fun initLayout() {
        binding.btnGoalNext.setOnClickListener {
            viewModel.isGoalSettingFinished(true)
        }
        val decimalFormat = DecimalFormat("#,###")
        var result = ""
        binding.etGoalSetting.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(txt: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!TextUtils.isEmpty(txt!!.toString()) && txt.toString() != result) {
                    //lessonPrice = txt.toString().replace(",", "").toInt()
                    result =
                        decimalFormat.format(txt.toString().replace(",", "").toDouble())
                    binding.etGoalSetting.setText(result)
                    binding.etGoalSetting.setSelection(result.length)
                }

//                if (TextUtils.isEmpty(txt.toString()) && txt.toString() != result) {
//                    result = ""
//                    binding.etGoalSetting.setText(result)
//                    binding.tvGoalSettingAmountInfo.apply {
//                        text = result
//                        visibility = View.INVISIBLE
//                    }
//                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }
}