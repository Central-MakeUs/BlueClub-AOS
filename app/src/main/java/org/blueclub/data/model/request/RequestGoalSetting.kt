package org.blueclub.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RequestGoalSetting(
    val yearMonth: String,
    val monthlyTargetIncome: Int,
)