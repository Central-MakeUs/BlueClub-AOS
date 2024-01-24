package org.blueclub.presentation.home

import android.view.View
import com.kizitonwose.calendar.view.ViewContainer
import org.blueclub.databinding.LayoutCalendarDayBinding

class DayViewContainer(view : View) : ViewContainer(view) {
    val textView = LayoutCalendarDayBinding.bind(view).tvCalendarDay
}