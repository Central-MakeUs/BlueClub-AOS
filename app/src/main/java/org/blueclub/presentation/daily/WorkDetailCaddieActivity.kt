package org.blueclub.presentation.daily

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.blueclub.R
import org.blueclub.databinding.ActivityWorkDetailCaddieBinding
import org.blueclub.presentation.base.BindingActivity
import java.text.DecimalFormat

@AndroidEntryPoint
class WorkDetailCaddieActivity :
    BindingActivity<ActivityWorkDetailCaddieBinding>(R.layout.activity_work_detail_caddie) {
    private val viewModel: DailyWorkDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        initLayout()
        collectData()
    }

    private fun initLayout() {
        intent.getStringExtra(ARG_DATE)?.let {
            viewModel.setDate(it)
        }
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.ivSelectWorkType.setOnClickListener {
            WorkTypeSettingBottomSheet().show(supportFragmentManager, "workTypeSetting")
        }
        binding.cbBaeto.setOnClickListener {
            viewModel.checkBaeto()
        }
        val decimalFormat = DecimalFormat("#,###")
        var resultCaddieP = ""
        binding.etAmountCaddieP.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(txt: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!TextUtils.isEmpty(txt!!.toString()) && txt.toString() != resultCaddieP) {
                    resultCaddieP =
                        decimalFormat.format(txt.toString().replace(",", "").toDouble())
                    binding.etAmountCaddieP.setText(resultCaddieP)
                    binding.etAmountCaddieP.setSelection(resultCaddieP.length)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
        var resultOverP = ""
        binding.etAmountOverP.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(txt: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!TextUtils.isEmpty(txt!!.toString()) && txt.toString() != resultOverP) {
                    resultOverP =
                        decimalFormat.format(txt.toString().replace(",", "").toDouble())
                    binding.etAmountOverP.setText(resultCaddieP)
                    binding.etAmountOverP.setSelection(resultCaddieP.length)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
        var spentAmount = ""
        binding.etExpenditure.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(txt: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!TextUtils.isEmpty(txt!!.toString()) && txt.toString() != spentAmount) {
                    spentAmount =
                        decimalFormat.format(txt.toString().replace(",", "").toDouble())
                    binding.etExpenditure.setText(spentAmount)
                    binding.etExpenditure.setSelection(spentAmount.length)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
        var savingsAmount = ""
        binding.etSaving.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(txt: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!TextUtils.isEmpty(txt!!.toString()) && txt.toString() != savingsAmount) {
                    savingsAmount =
                        decimalFormat.format(txt.toString().replace(",", "").toDouble())
                    binding.etSaving.setText(savingsAmount)
                    binding.etSaving.setSelection(savingsAmount.length)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

    }

    private fun collectData() {
    }

    companion object {
        const val ARG_DATE = "date"
        const val ARG_YEAR = "year"
        const val ARG_MONTH = "month"
        const val ARG_DAY = "day"
    }
}