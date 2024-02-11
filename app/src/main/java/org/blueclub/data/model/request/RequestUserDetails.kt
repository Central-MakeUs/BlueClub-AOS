package org.blueclub.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RequestUserDetails(
    val nickname: String,
    val job: String,
    val monthlyTargetIncome: Int,
    val tosAgree: Boolean,
    val pushAgree: Boolean,
)
