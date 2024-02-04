package org.blueclub.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ResponseBase(
    val code: String,
    val message: String,
    val result: String?,
)
