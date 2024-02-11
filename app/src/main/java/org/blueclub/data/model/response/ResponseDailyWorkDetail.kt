package org.blueclub.data.model.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDailyWorkDetail(
    val code: String,
    val message: String,
    val result: ResponseDailyWorkDetailData,
) {
    @Serializable
    data class ResponseDailyWorkDetailData(
        @SerializedName("worktype")
        val workType: String,
        val memo: String,
        val imageUrlList: List<String>,
        val income: Int,
        val expenditure: Int,
        val place: String,
    )
}