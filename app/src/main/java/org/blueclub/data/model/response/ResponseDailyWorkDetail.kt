package org.blueclub.data.model.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDailyWorkDetail(
    val code: String,
    val message: String,
    val result: ResponseDailyWorkDetailData?,
) {
    @Serializable
    data class ResponseDailyWorkDetailData(
        val id: Int,
        @SerializedName("worktype")
        val workType: String,
        val memo: String?,
        val imageUrlList: List<String>,
        val income: Int,
        val expenditure: Int,
        val saving: Int,
        val rounding: Int,
        val caddyFee: Int,
        val overFee: Int,
        @SerializedName("topdressing")
        val topDressing: Boolean,
    )
}