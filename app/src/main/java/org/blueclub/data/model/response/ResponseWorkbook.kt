package org.blueclub.data.model.response

import com.google.gson.annotations.SerializedName
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
            @SerializedName("worktype")
            val workType: String,
            val income: Int,
            val cases: Int,
        ) {
            fun toDailyWorkData() = DailyWorkInfo(
                id,
                date,
                getDayOfWeek(date),
                getDailyWorkType(workType),
                income,
                cases,
                getDay(date),
            )

            fun getDailyWorkType(name: String?): DailyWorkType {
                if (name == null) return DailyWorkType.DEFAULT
                DailyWorkType.values().onEach {
                    if (it.title == name)
                        return it
                }
                return DailyWorkType.DEFAULT
            }

            fun getDayOfWeek(date: String): String {
                val cal: Calendar = Calendar.getInstance()
                val df = SimpleDateFormat(dateFormat)
                var day = Date()
                day = df.parse(date) ?: return ""
                cal.time = day
                return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.KOREAN) ?: ""

            }

            private fun getDay(date: String): Int {
                return date.slice(8..9).toIntOrNull() ?: 0
            }

            companion object {
                var dateFormat = "yyyy-MM-dd"
            }
        }
    }
}

