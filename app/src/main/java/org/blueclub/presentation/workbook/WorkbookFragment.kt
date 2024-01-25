package org.blueclub.presentation.workbook

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
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.MonthScrollListener
import com.kizitonwose.calendar.view.ViewContainer
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.blueclub.R
import org.blueclub.databinding.FragmentWorkbookBinding
import org.blueclub.domain.model.DailyWorkInfo
import org.blueclub.presentation.base.BindingFragment
import org.blueclub.presentation.type.DailyWorkType
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class WorkbookFragment : BindingFragment<FragmentWorkbookBinding>(R.layout.fragment_workbook) {
    private val viewModel: WorkbookViewModel by activityViewModels()
    private lateinit var rootView: View
    private lateinit var dailyWorkAdapter: DailyWorkAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        rootView = binding.root

        initLayout()
        initCalendar()
        collectData()
    }

    private fun initLayout() {
        dailyWorkAdapter = DailyWorkAdapter()
        binding.rvDailyRecord.apply {
            itemAnimator = null
            adapter = dailyWorkAdapter
        }
        dailyWorkAdapter.submitList(
            listOf(
                DailyWorkInfo("1.2", "월", DailyWorkType.WORK, 15000000, 8),
                DailyWorkInfo("1.3", "화", DailyWorkType.REST, null, null),
                DailyWorkInfo("1.5", "수", DailyWorkType.EARLY, 15000000, 8),
                DailyWorkInfo("1.8", "목", DailyWorkType.WORK, 15000000, 8),
                DailyWorkInfo("1.9", "금", DailyWorkType.WORK, 15000000, 8),
                DailyWorkInfo("1.10", "월", DailyWorkType.WORK, 15000000, 8),
            )
        )
    }

    private fun initCalendar() {
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                if (data.position == DayPosition.MonthDate) {
                    container.tvDay.text = data.date.dayOfMonth.toString()
                    container.tvDay.background =
                        AppCompatResources.getDrawable(requireContext(), R.drawable.ic_coin)
                } else { // 이번 달이 아닌 경우
                    container.tvDay.background = null
                    container.tvAmount.text = ""
                }
            }

            override fun create(view: View): DayViewContainer = DayViewContainer(view)

        }
        val currentMonthPage = YearMonth.now()
        val startMonth = currentMonthPage.minusMonths(12)
        val firstDayOfWeek = firstDayOfWeekFromLocale()
        binding.calendarView.setup(startMonth, currentMonthPage, firstDayOfWeek)
        binding.ivCalendarPrevMonth.setOnClickListener {
            viewModel.setYearMonth(viewModel.yearMonth.value.previousMonth)
        }
        binding.ivCalendarNextMonth.setOnClickListener {
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
        //binding.calendarView.rootView.isNestedScrollingEnabled = false
        binding.calendarView.isNestedScrollingEnabled = false
        binding.calendarView.scrollToMonth(YearMonth.now())
    }

    private fun collectData() {
        viewModel.goalProgress.flowWithLifecycle(lifecycle).onEach {
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
        }.launchIn(lifecycleScope)
    }

    private fun getProgressBarSize(): Int {
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)
        val screenWidth = rootView.rootView.width
        return screenWidth - binding.pbGoal.marginLeft * 4
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