package org.blueclub.data.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.blueclub.data.model.response.ResponseCaddieDiary
import org.blueclub.data.model.response.ResponseMonthlyInfo
import org.blueclub.data.model.response.ResponseWorkbook
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface WorkbookService {

    @Multipart
    @POST("diary")
    suspend fun uploadCaddieRecord(
        @Query("job") jobName: String,
        @Part("dto") caddieDiary: RequestBody,
        @Part image: MultipartBody.Part?
    ): ResponseCaddieDiary

    @GET("diary/list/{yearMonth}")
    suspend fun getMonthlyRecord(@Path("yearMonth") date: String): ResponseWorkbook

    @GET("diary/{diaryId}")
    suspend fun getDetailRecord(@Path("diaryId") diaryId: String)


    @GET("diary/record/{yearMonth}")
    suspend fun getMonthlyInfo(@Path("yearMonth") date: String): ResponseMonthlyInfo
}