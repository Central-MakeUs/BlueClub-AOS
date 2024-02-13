package org.blueclub.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ResponseCard(
    val code: String,
    val message:String,
    val result: ResponseCardData,
){
    @Serializable
    data class ResponseCardData(
        val job: String,
        val workAt: String,
        val rank: String,
        val income: Int,
        val cases: Int?
    )
}
