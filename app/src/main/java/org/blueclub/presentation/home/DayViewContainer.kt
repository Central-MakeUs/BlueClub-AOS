package org.blueclub.presentation.home

import android.view.View
import com.kizitonwose.calendar.view.ViewContainer
import org.blueclub.databinding.LayoutCalendarDayBinding

class DayViewContainer(view: View) : ViewContainer(view) {
    val tvDay = LayoutCalendarDayBinding.bind(view).tvCalendarDay
    val tvAmount = LayoutCalendarDayBinding.bind(view).tvDailyIncome
}