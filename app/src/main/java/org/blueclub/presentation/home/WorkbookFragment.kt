package org.blueclub.presentation.home

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
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
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.blueclub.R
import org.blueclub.databinding.FragmentWorkbookBinding
import org.blueclub.presentation.base.BindingFragment
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class WorkbookFragment : BindingFragment<FragmentWorkbookBinding>(R.layout.fragment_workbook) {
    private val viewModel: WorkbookViewModel by activityViewModels()
    lateinit var rootView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        rootView = binding.root

        initLayout()
        collectData()
    }

    private fun initLayout() {
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                if (data.position == DayPosition.MonthDate) {
                    container.textView.text = data.date.dayOfMonth.toString()
                }
            }

            // TODO outdate 일정의 경우 안 보이도록 수정하기
            override fun create(view: View): DayViewContainer = DayViewContainer(view)

        }
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)  // Adjust as needed
        val endMonth = currentMonth.plusMonths(100)  // Adjust as needed
        val firstDayOfWeek = firstDayOfWeekFromLocale() // Available from the library
        binding.calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(currentMonth)


        // TODO 코드 리팩토링 필요
//        val titleContainer = requireActivity().findViewById<ViewGroup>(R.id.layout_title_container)
//        titleContainer.children
//            .map { it as TextView }
//            .forEachIndexed { index, textView ->
//                val dayOfWeek = daysOfWeek()[index]
//                val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
//                textView.text = title
//            }

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