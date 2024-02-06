package org.blueclub.domain.model

data class MonthlyInfo(
    val totalDay: Int,
    val straightDay: Int,
    val isRenew: Boolean,
    val straightMonth: Int,
    val targetIncome: Int,
    val totalIncome: Int,
    val progress: Int,
)
