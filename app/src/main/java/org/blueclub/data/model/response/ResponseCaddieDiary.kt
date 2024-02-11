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
        val job: String,
        val workAt: String,
        val rank: String,
        val income: Int,
        val cases: Int?,
    )
}
