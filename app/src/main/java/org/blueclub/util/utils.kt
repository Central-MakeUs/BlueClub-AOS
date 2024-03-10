package org.blueclub.util

import org.blueclub.presentation.daily.caddie.WorkDetailCaddieViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getDayOfWeek(date: String): String {
    val cal: Calendar = Calendar.getInstance()
    val df = SimpleDateFormat(WorkDetailCaddieViewModel.dateFormat)
    var day = Date()
    day = df.parse(date) ?: return ""
    cal.time = day
    return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.KOREAN) ?: ""
}