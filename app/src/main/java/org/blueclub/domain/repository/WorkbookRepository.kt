package org.blueclub.domain.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.blueclub.data.model.response.ResponseCaddieDiary
import org.blueclub.data.model.response.ResponseMonthlyInfo
import org.blueclub.data.model.response.ResponseWorkbook

interface WorkbookRepository {

    suspend fun uploadCaddieDiary(
        job: String,
        requestCaddieDiary: RequestBody,
        image: MultipartBody.Part?,
    ): Result<ResponseCaddieDiary>

    suspend fun getMonthlyRecord(date: String): Result<ResponseWorkbook.ResponseWorkbookData>

    suspend fun getMonthlyInfo(date: String): Result<ResponseMonthlyInfo.ResponseMonthlyInfoData>
}