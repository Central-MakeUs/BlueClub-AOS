package org.blueclub.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RequestAgreement(
    val tosAgree: Boolean,
    val pushAgree: Boolean,
)
