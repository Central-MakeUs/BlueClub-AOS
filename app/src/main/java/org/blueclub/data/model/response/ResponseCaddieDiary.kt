package org.blueclub.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ResponseCaddieDiary(
    val code: String,
    val message: String,
    val result: ResponseCaddieDiaryData,
) {
    @Serializable
    data class ResponseCaddieDiaryData(
        val id: Int,
    )
}
