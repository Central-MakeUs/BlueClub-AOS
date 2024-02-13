package org.blueclub.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RequestAuth(
    val socialId: String,
    val socialType: String,
    val name: String,
    val nickname: String,
    val email: String,
    val phoneNumber: String,
    val profileImage: String,
    val fcmToken: String?,
)