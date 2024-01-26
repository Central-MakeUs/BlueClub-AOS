package org.blueclub.presentation.daily

import android.os.Bundle
import org.blueclub.R
import org.blueclub.databinding.ActivityDailyWorkDetailBinding
import org.blueclub.presentation.base.BindingActivity
import org.blueclub.presentation.model.DailyWorkDetailOption
import org.blueclub.presentation.type.DailyWorkDetailOptionType

class DailyWorkDetailActivity :
    BindingActivity<ActivityDailyWorkDetailBinding>(R.layout.activity_daily_work_detail) {
    private lateinit var dailyworkAdapter: DailyWorkDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initLayout()
    }

    private fun initLayout() {
        dailyworkAdapter = DailyWorkDetailAdapter(::showWorkTypeDialog)
        binding.rvDailyWorkDetail.apply {
            itemAnimator = null
            adapter = dailyworkAdapter
        }
        dailyworkAdapter.submitList(
            listOf(
                DailyWorkDetailOption(
                    DailyWorkDetailOptionType.WORK_TYPE,
                    R.string.daily_work_type, true
                ),
                DailyWorkDetailOption(
                    DailyWorkDetailOptionType.WORK_AMOUNT,
                    R.string.daily_caddie_rounding, true
                ),
                DailyWorkDetailOption(
                    DailyWorkDetailOptionType.MONEY_INPUT,
                    R.string.daily_caddie_p, true
                ),
                DailyWorkDetailOption(
                    DailyWorkDetailOptionType.MONEY_INPUT,
                    R.string.daily_caddie_over_p, false
                ),
                DailyWorkDetailOption(
                    DailyWorkDetailOptionType.CHECKBOX,
                    R.string.daily_caddie_soil, false
                ),
            )
        )
    }

    private fun showWorkTypeDialog() {}

    private fun showMemoDialog() {}
}