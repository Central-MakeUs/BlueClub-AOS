package org.blueclub.data.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.blueclub.data.datasource.remote.WorkbookDataSource
import org.blueclub.data.model.request.RequestCaddieDiary
import org.blueclub.data.model.response.ResponseCaddieDiary
import org.blueclub.data.model.response.ResponseMonthlyInfo
import org.blueclub.data.model.response.ResponseWorkbook
import org.blueclub.domain.repository.WorkbookRepository
import timber.log.Timber
import javax.inject.Inject

class WorkbookRepositoryImpl @Inject constructor(
    private val workbookDataSource: WorkbookDataSource,
) : WorkbookRepository {
    override suspend fun uploadCaddieDiary(
        job: String,
        requestCaddieDiary: RequestBody,
        image: MultipartBody.Part?,
    ): Result<ResponseCaddieDiary> =
        runCatching {
            workbookDataSource.uploadCaddieDiary(job, requestCaddieDiary, image)
        }.onFailure {
            Timber.d(it)
        }

    override suspend fun getMonthlyRecord(date: String): Result<ResponseWorkbook.ResponseWorkbookData> =
        runCatching {
            workbookDataSource.getMonthlyRecord(date).result
        }.onFailure {
            Timber.d(it)
        }

    override suspend fun getMonthlyInfo(date: String): Result<ResponseMonthlyInfo.ResponseMonthlyInfoData> =
        runCatching {
            workbookDataSource.getMonthlyInfo(date).result
        }.onFailure {
            Timber.d(it)
        }
}