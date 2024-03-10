package org.blueclub.presentation.daily.rider

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.blueclub.R
import org.blueclub.databinding.ActivityWorkDetailRiderBinding
import org.blueclub.presentation.base.BindingActivity
import org.blueclub.presentation.card.WorkCardLoadingActivity
import org.blueclub.presentation.daily.WorkTypeSettingBottomSheet
import org.blueclub.presentation.daily.caddie.WorkDetailCaddieActivity
import org.blueclub.util.UiState
import org.blueclub.util.extension.showToast
import timber.log.Timber
import java.text.DecimalFormat

@AndroidEntryPoint
class WorkDetailRiderActivity :
    BindingActivity<ActivityWorkDetailRiderBinding>(R.layout.activity_work_detail_rider) {
    private val viewModel: WorkDetailRiderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initLayout()
        collectData()
    }

    private fun initLayout() {
        intent.getStringExtra(WorkDetailCaddieActivity.ARG_DATE)?.let {
            Timber.d("날짜: $it")
            viewModel.setDate(it)
            viewModel.getRiderWorkBook()
        }
        intent.getIntExtra(WorkDetailCaddieActivity.ARG_GOAL, 0).let{
            Timber.d("목표금액: $it")
            viewModel.incomeGoal.value = it
        }
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnShow.setOnClickListener {
            viewModel.uploadRiderWorkBook(false)
        }
        binding.tvSave.setOnClickListener {
            viewModel.uploadRiderWorkBook(true)
        }
        binding.ivSelectWorkType.setOnClickListener {
            RiderWorkTypeSettingBottomSheet().show(supportFragmentManager, "workTypeSetting")
        }
        binding.tvDailyWorkType.setOnClickListener {
            WorkTypeSettingBottomSheet().show(supportFragmentManager, "workTypeSetting")
        }
        binding.layoutAddMemo.setOnClickListener {
            MemoRiderBottomSheet().show(supportFragmentManager, "memo")
        }
        binding.tvMemo.setOnClickListener {
            MemoRiderBottomSheet().show(supportFragmentManager, "memo")
        }
        val decimalFormat = DecimalFormat("#,###")
        var resultDeliveryIncome = ""
        binding.etAmountDeliveryIncome.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(txt: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!TextUtils.isEmpty(txt!!.toString()) && txt.toString() != resultDeliveryIncome) {
                    resultDeliveryIncome =
                        decimalFormat.format(txt.toString().replace(",", "").toDouble())
                    binding.etAmountDeliveryIncome.setText(resultDeliveryIncome)
                    binding.etAmountDeliveryIncome.setSelection(resultDeliveryIncome.length)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.calculateIncome()
            }

        })

        var resultPromotionIncome = ""
        binding.etAmountPromotionIncome.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(txt: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!TextUtils.isEmpty(txt!!.toString()) && txt.toString() != resultPromotionIncome) {
                    resultPromotionIncome =
                        decimalFormat.format(txt.toString().replace(",", "").toDouble())
                    binding.etAmountPromotionIncome.setText(resultPromotionIncome)
                    binding.etAmountPromotionIncome.setSelection(resultPromotionIncome.length)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.calculateIncome()
            }

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
        viewModel.isUploadedUiState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Success -> {
                    if (!it.data) // 자랑하기에서 온 경우
                        moveToCardLoading(viewModel.workId.value)
                    else
                        finish()
                }

                is UiState.Error -> {
                    this.showToast("저장에 실패했습니다.")
                }

                else -> {}
            }
        }.launchIn(lifecycleScope)

    }

    private fun moveToCardLoading(workBookId: Int?) {
        Intent(this, WorkCardLoadingActivity::class.java).apply {
            putExtra(WorkCardLoadingActivity.ARG_WORK_BOOK_ID, workBookId)
        }.also { startActivity(it) }
        finish()
    }

    companion object {
        const val ARG_DATE = "date"
        const val ARG_GOAL = "goal"
    }
}