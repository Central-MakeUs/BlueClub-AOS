package org.blueclub.domain.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.blueclub.data.model.request.RequestGoalSetting
import org.blueclub.data.model.response.ResponseBase
import org.blueclub.data.model.response.ResponseCaddieDiary
import org.blueclub.data.model.response.ResponseCard
import org.blueclub.data.model.response.ResponseDailyWorkDetail
import org.blueclub.data.model.response.ResponseMonthlyInfo
import org.blueclub.data.model.response.ResponseWorkbook

interface WorkbookRepository {

    suspend fun uploadCaddieDiary(
        job: String,
        requestCaddieDiary: RequestBody,
        image: MultipartBody.Part?,
    ): Result<ResponseCaddieDiary>

    suspend fun modifyCaddieDiary(
        workId: Int,
        jobName: String,
        requestCaddieDiary: RequestBody,
        image: MultipartBody.Part?,
    ): Result<ResponseCaddieDiary>

    suspend fun getMonthlyRecord(date: String): Result<ResponseWorkbook.ResponseWorkbookData>

    suspend fun getMonthlyInfo(date: String): Result<ResponseMonthlyInfo.ResponseMonthlyInfoData>

    suspend fun getDetailRecord(
        jobName: String,
        date: String
    ): Result<ResponseDailyWorkDetail.ResponseDailyWorkDetailData?>

    suspend fun uploadMonthlyGoal(requestGoalSetting: RequestGoalSetting): Result<ResponseBase>

    suspend fun getCardDetail(workId: Int): Result<ResponseCard.ResponseCardData>
}