package org.blueclub.data.datasource.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.blueclub.data.model.request.RequestCaddieDiary
import org.blueclub.data.service.WorkbookService
import javax.inject.Inject

class WorkbookDataSource @Inject constructor(
    private val workbookService: WorkbookService,
) {
    suspend fun uploadCaddieDiary(
        job: String,
        requestCaddieDiary: RequestBody,
        image: MultipartBody.Part?,
    ) = workbookService.uploadCaddieRecord(job, requestCaddieDiary, image)

    suspend fun getMonthlyRecord(date: String) = workbookService.getMonthlyRecord(date)

    suspend fun getMonthlyInfo(date: String) = workbookService.getMonthlyInfo(date)
}