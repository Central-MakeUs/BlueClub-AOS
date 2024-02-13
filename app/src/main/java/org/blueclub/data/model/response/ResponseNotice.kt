package org.blueclub.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ResponseNotice(
    val code: String,
    val message: String,
    val result: List<ResponseNoticeData>,
) {
    @Serializable
    data class ResponseNoticeData(
        val id: Int,
        val title: String,
        val content: String,
        val createAt: String,
    )
}
