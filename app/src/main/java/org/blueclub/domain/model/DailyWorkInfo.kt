package org.blueclub.domain.model

import org.blueclub.presentation.type.DailyWorkType

data class DailyWorkInfo(
    val id: Int,
    val date: String,
    val dayOfWeek: String,
    val dailyWorkType: DailyWorkType,
    val income:Int?,
    val numberOfCases:Int?,
    val day: Int,
)