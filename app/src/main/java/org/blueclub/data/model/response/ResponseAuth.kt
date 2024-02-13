package org.blueclub.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ResponseAuth(
    val code: String,
    val message: String,
    val result: ResponseAuthData,
) {
    @Serializable
    data class ResponseAuthData(
        val role: String?,
        val tosAgree: Boolean?,
        val socialType: String?,
        val monthlyTargetIncome: Int?,
        val profileImage: String?,
        val accessToken: String,
        val phoneNumber: String?,
        val socialId: String?,
        val nickname: String,
        val name: String?,
        val id: Int,
        val job: String?,
        val email: String?,
        val refreshToken: String,
    )
}