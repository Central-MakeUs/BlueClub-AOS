package org.blueclub.presentation.workbook

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.children
import androidx.core.view.marginLeft
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.MonthScrollListener
import com.kizitonwose.calendar.view.ViewContainer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.blueclub.R
import org.blueclub.databinding.FragmentWorkbookBinding
import org.blueclub.presentation.base.BindingFragment
import org.blueclub.presentation.daily.caddie.WorkDetailCaddieActivity
import org.blueclub.presentation.daily.daylabor.WorkDetailDayLaborActivity
import org.blueclub.presentation.daily.rider.WorkDetailRiderActivity
import org.blueclub.presentation.notice.NoticeActivity
import org.blueclub.util.UiState
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@AndroidEntryPoint
class WorkbookFragment : BindingFragment<FragmentWorkbookBinding>(R.layout.fragment_workbook) {
    private val viewModel: WorkbookViewModel by activityViewModels()
    private lateinit var rootView: View
    private lateinit var dailyWorkAdapter: DailyWorkAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        rootView = binding.root

        initCalendar()
        initLayout()
        collectData()
    }

    private fun initLayout() {
        dailyWorkAdapter = DailyWorkAdapter(::moveToDetail)
        binding.rvDailyRecord.apply {
            itemAnimator = null
            adapter = dailyWorkAdapter
        }
        binding.ivSetting.setOnClickListener {
            showGoalSettingBottomSheet()
        }
        binding.layoutGoalSetting.setOnClickListener {
            showGoalSettingBottomSheet()
        }
        binding.ivPlus.setOnClickListener {
            moveToDetail(LocalDate.now().toString())
        }
        binding.ivNotice.setOnClickListener {
            moveToNotice()
        }
        lifecycleScope.launch {
            delay(10L)
            binding.calendarView.scrollToMonth(YearMonth.now())
        }
    }

    private fun initCalendar() {
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                if (data.position == DayPosition.MonthDate) { // 이번 달에 해당하는 뷰
                    container.view.setOnClickListener {
                        var date = "${data.date.yearMonth.year}-"
                        if (data.date.yearMonth.monthValue < 10) {
                            date += "0"
                        }
                        date += "${data.date.yearMonth.monthValue}-"
                        if (data.date.dayOfMonth < 10) {
                            date += "0"
                        }
                        date += data.date.dayOfMonth
                        moveToDetail(date)
                    }
                    container.tvDay.text = data.date.dayOfMonth.toString()
                    if (viewModel.workData.value[data.date.dayOfMonth] != null) {
                        container.tvDay.background =
                            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_coin)

                        container.tvAmount.setTextColor(requireContext().getColor(R.color.coolgray_06))
                        container.tvAmount.text =
                            ((viewModel.workData.value[data.date.dayOfMonth]?.income
                                ?: 0) / 10000).toString() + " 만원"
                        container.tvDay.setTextColor(requireContext().getColor(R.color.white))
                    } else {
                        container.tvDay.background =
                            AppCompatResources.getDrawable(
                                requireContext(),
                                R.drawable.ic_coin_gray
                            )
                        // 오늘인 경우
                        if (viewModel.yearMonth.value.monthValue == YearMonth.now().monthValue
                            && viewModel.yearMonth.value.year == YearMonth.now().year
                            && LocalDateTime.now().dayOfMonth == data.date.dayOfMonth
                        ) {
                            container.tvAmount.text = "Today"
                            container.tvAmount.setTextColor(requireContext().getColor(R.color.primary_normal))
                            container.tvDay.background =
                                AppCompatResources.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_coin_empty
                                )
                        } else {
                            container.tvAmount.setTextColor(requireContext().getColor(R.color.coolgray_06))
                            container.tvAmount.text = ""
                        }

                        container.tvDay.setTextColor(requireContext().getColor(R.color.gray_08))
                    }


                } else { // 이번 달이 아닌 경우
                    container.tvAmount.setTextColor(requireContext().getColor(R.color.coolgray_06))
                    container.tvDay.background = null
                    container.tvDay.text = ""
                    container.tvAmount.text = ""
                }
            }

            override fun create(view: View): DayViewContainer = DayViewContainer(view)

        }
        val currentMonthPage = YearMonth.now()
        val startMonth = currentMonthPage.minusYears(3)
        val endMonth = currentMonthPage.plusYears(3)
        val firstDayOfWeek = firstDayOfWeekFromLocale()
        binding.calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        binding.ivCalendarPrevMonth.setOnClickListener {
            if (viewModel.yearMonth.value.previousMonth >= startMonth)
                viewModel.setYearMonth(viewModel.yearMonth.value.previousMonth)
        }
        binding.ivCalendarNextMonth.setOnClickListener {
            if (viewModel.yearMonth.value.nextMonth <= endMonth)
                viewModel.setYearMonth(viewModel.yearMonth.value.nextMonth)
        }
        binding.calendarView.monthScrollListener = object : MonthScrollListener {
            override fun invoke(calendar: CalendarMonth) {
                viewModel.setYearMonth(calendar.yearMonth)
            }
        }

        binding.calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    if (container.titlesContainer.tag == null) {
                        container.titlesContainer.tag = data.yearMonth
                        container.titlesContainer.children.map { it as TextView }
                            .forEachIndexed { index, textView ->
                                val dayOfWeek = daysOfWeek()[index]
                                val title =
                                    dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                                textView.text = title
                            }
                    }
                }

                override fun create(view: View): MonthViewContainer = MonthViewContainer(view)

            }

    }

    private fun collectData() {
        viewModel.progress.flowWithLifecycle(lifecycle).onEach {
            var leftMargin = getProgressBarSize() * it / 100 - PROGRESS_MARGIN_MIN
            if (leftMargin < PROGRESS_MARGIN_MIN)
                leftMargin = PROGRESS_MARGIN_MIN
            if (leftMargin > getProgressBarSize() - PROGRESS_MARGIN_MAX)
                leftMargin = getProgressBarSize() - PROGRESS_MARGIN_MAX
            var bubbleMargin = leftMargin - BUBBLE_WIDTH
            if (bubbleMargin < PROGRESS_BUBBLE_MARGIN_MIN)
                bubbleMargin = PROGRESS_BUBBLE_MARGIN_MIN
            if (bubbleMargin > getProgressBarSize() - PROGRESS_BUBBLE_MARGIN_MAX)
                bubbleMargin = getProgressBarSize() - PROGRESS_BUBBLE_MARGIN_MAX
            binding.ivBubbleBelow.updateLayoutParams<MarginLayoutParams> {
                this.marginStart = leftMargin
            }
            binding.tvBubbleAmount.updateLayoutParams<MarginLayoutParams> {
                this.marginStart = bubbleMargin
            }
            binding.tvBubbleAmount.text = "$it%"
        }.launchIn(lifecycleScope)
        viewModel.yearMonth.flowWithLifecycle(lifecycle).onEach {
            binding.calendarView.smoothScrollToMonth(it)
            binding.calendarView.notifyMonthChanged(viewModel.yearMonth.value)
            viewModel.setWorkData(mapOf())
            viewModel.setMonthlyInfoUiState(UiState.Loading)
            viewModel.setMonthlyRecordUiState(UiState.Loading)
        }.launchIn(lifecycleScope)
        viewModel.monthlyRecordUiState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> {
                    val yM = viewModel.yearMonth.value
                    var yearMonth = "${yM.year}-"
                    if (yM.monthValue < 10) {
                        yearMonth += "0"
                    }
                    yearMonth += yM.monthValue
                    viewModel.getMonthlyRecord(yearMonth)
                }

                is UiState.Success -> {
                    dailyWorkAdapter.submitList(it.data)
                    binding.calendarView.notifyCalendarChanged()
                }

                else -> {}
            }
        }.launchIn(lifecycleScope)
        viewModel.monthlyInfoUiState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> {
                    viewModel.getMonthlyInfo()
                }

                is UiState.Success -> {
                    binding.calendarView.notifyMonthChanged(viewModel.yearMonth.value)
                }

                else -> {}
            }
        }.launchIn(lifecycleScope)
    }

    private fun getProgressBarSize(): Int {
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)
        val screenWidth = rootView.rootView.width
        return screenWidth - binding.pbGoal.marginLeft * 4
    }

    private fun showGoalSettingBottomSheet() {
        GoalSettingBottomSheet().show(parentFragmentManager, "goalSetting")
    }

    private fun moveToDetail(date: String) { // yyyy-mm-dd
        Timber.d("직업: ${viewModel.job}")
        Timber.d("목표금액: ${viewModel.incomeGoal.value?.replace(",","")?.toIntOrNull() ?: 0}")
        if (viewModel.job.toString() == "골프캐디") {
            Intent(requireActivity(), WorkDetailCaddieActivity::class.java).apply {
                putExtra(WorkDetailCaddieActivity.ARG_DATE, date)
                putExtra(WorkDetailCaddieActivity.ARG_GOAL, viewModel.incomeGoal.value?.replace(",","")?.toIntOrNull() ?: 0)
            }.also { startActivity(it) }
        } else if (viewModel.job.toString() == "배달라이더") {
            Intent(requireActivity(), WorkDetailRiderActivity::class.java).apply {
                putExtra(WorkDetailRiderActivity.ARG_DATE, date)
                putExtra(WorkDetailRiderActivity.ARG_GOAL, viewModel.incomeGoal.value?.replace(",","")?.toIntOrNull() ?: 0)
            }.also { startActivity(it) }
        } else if (viewModel.job.toString() == "일용직 근로자" || viewModel.job.toString() == "일용직근로자") {
            Intent(requireActivity(), WorkDetailDayLaborActivity::class.java).apply {
                putExtra(WorkDetailDayLaborActivity.ARG_DATE, date)
                putExtra(WorkDetailDayLaborActivity.ARG_GOAL, viewModel.incomeGoal.value?.replace(",","")?.toIntOrNull() ?: 0)
            }.also { startActivity(it) }
        }

    }

    private fun moveToNotice() {
        startActivity(Intent(requireActivity(), NoticeActivity::class.java))
    }

    companion object {
        const val PROGRESS_MARGIN_MIN = 15
        const val PROGRESS_MARGIN_MAX = 30
        const val PROGRESS_BUBBLE_MARGIN_MIN = 0
        const val PROGRESS_BUBBLE_MARGIN_MAX = 110
        const val BUBBLE_WIDTH = 40
    }

}

class MonthViewContainer(view: View) : ViewContainer(view) {
    // Alternatively, you can add an ID to the container layout and use findViewById()
    val titlesContainer = view as ViewGroup
}