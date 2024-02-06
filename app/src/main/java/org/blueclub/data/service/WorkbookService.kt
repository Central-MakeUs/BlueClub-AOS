package org.blueclub.data.service

import org.blueclub.data.model.request.RequestCaddieDiary
import org.blueclub.data.model.response.ResponseCaddieDiary
import org.blueclub.data.model.response.ResponseMonthlyInfo
import org.blueclub.data.model.response.ResponseWorkbook
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WorkbookService {

    @GET("diary/list/{yearMonth}")
    suspend fun getMonthlyRecord(@Path("yearMonth") date: String): ResponseWorkbook

    @GET("diary/{diaryId}")
    suspend fun getDetailRecord(@Path("diaryId") diaryId: String)

    @POST("diary")
    suspend fun uploadCaddieRecord(
        @Query("job") jobName: String,
        @Body caddieDiary: RequestCaddieDiary
    ): ResponseCaddieDiary

    @GET("diary/record/{yearMonth}")
    suspend fun getMonthlyInfo(@Path("yearMonth") date: String): ResponseMonthlyInfo
}