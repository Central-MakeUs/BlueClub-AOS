package org.blueclub.data.service

import org.blueclub.data.model.response.ResponseWorkbook
import retrofit2.http.GET
import retrofit2.http.Path

interface WorkbookService {

    @GET("diary/record/{yearMonth}")
    suspend fun getMonthlyRecord(@Path("yearMonth") date: String): ResponseWorkbook
}