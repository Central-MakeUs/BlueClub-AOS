package org.blueclub.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody


@Serializable
data class RequestDayLaborDiary(
    @SerialName("worktype")
    val workType: String,
    val memo: String,
    val income: Int, // 총 수입
    val expenditure: Int,
    val saving: Int,
    val date: String, // yyyy-mm-dd
    val imageUrlList: List<String>,
    val place: String,
    val dailyWage: Int,
    val typeOfJob: String,
    val numberOfWork: Double,
){
    fun toJsonObject() = buildJsonObject {
        put("worktype", workType)
        put("memo", memo)
        put("income", income)
        put("expenditure", expenditure)
        put("saving", saving)
        put("date", date)
        put("place", place)
        put("dailyWage", dailyWage)
        put("typeOfJob", typeOfJob)
        put("numberOfWork", numberOfWork)
    }.toString().toRequestBody("application/json".toMediaType())
}
