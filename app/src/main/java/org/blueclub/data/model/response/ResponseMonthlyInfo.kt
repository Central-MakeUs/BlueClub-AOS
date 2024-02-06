package org.blueclub.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ResponseMonthlyInfo(
    val code: String,
    val message: String,
    val result: ResponseMonthlyInfoData,
) {
    @Serializable
    data class ResponseMonthlyInfoData(
        val totalDay: Int,
        val straightDay: Int,
        val isRenew: Boolean,
        val straightMonth: Int,
        val targetIncome: Int,
        val totalIncome: Int,
        val progress: Int,
    )
}
