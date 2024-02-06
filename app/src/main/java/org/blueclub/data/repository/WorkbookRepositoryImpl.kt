package org.blueclub.data.repository

import org.blueclub.data.datasource.remote.WorkbookDataSource
import org.blueclub.data.model.response.ResponseMonthlyInfo
import org.blueclub.data.model.response.ResponseWorkbook
import org.blueclub.domain.repository.WorkbookRepository
import timber.log.Timber
import javax.inject.Inject

class WorkbookRepositoryImpl @Inject constructor(
    private val workbookDataSource: WorkbookDataSource,
) : WorkbookRepository {
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