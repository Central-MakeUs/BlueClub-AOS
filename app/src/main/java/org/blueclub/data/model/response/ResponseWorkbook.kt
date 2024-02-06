package org.blueclub.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.blueclub.domain.model.DailyWorkInfo
import org.blueclub.presentation.type.DailyWorkType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Serializable
data class ResponseWorkbook(
    val code: String,
    val message: String,
    val result: ResponseWorkbookData,
) {
    @Serializable
    data class ResponseWorkbookData(
        val totalWorkingDay: Int,
        val monthlyRecord: List<DailyData>,
    ) {
        @Serializable
        data class DailyData(
            val id: Int,
            val date: String,
            @SerialName("worktype")
            val workType: String,
            val income: Int,
            val numberOfCases: Int,
        ) {
            fun toDailyWorkData() = DailyWorkInfo(
                id,
                date,
                getDay(date),
                DailyWorkType.valueOf(workType),
                income,
                numberOfCases
            )

            fun getDay(date: String): String {
                val cal: Calendar = Calendar.getInstance()
                val df = SimpleDateFormat(dateFormat)
                var day = Date()
                day = df.parse(date) ?: return ""
                cal.time = day
                return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.KOREAN) ?: ""

            }

            companion object {
                var dateFormat = "yyyy-MM-dd"
            }
        }
    }
}

