package org.blueclub.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RequestModifyUserDetails(
    val nickname: String,
    val job: String,
    val monthlyTargetIncome: Int,
)
