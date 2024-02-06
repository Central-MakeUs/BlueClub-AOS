package org.blueclub.data.datasource.remote

import org.blueclub.data.service.WorkbookService
import javax.inject.Inject

class WorkbookDataSource @Inject constructor(
    private val workbookService: WorkbookService,
) {
    suspend fun getMonthlyRecord(date: String) = workbookService.getMonthlyRecord(date)

    suspend fun getMonthlyInfo(date: String) = workbookService.getMonthlyInfo(date)
}