package org.blueclub.domain.repository

import org.blueclub.data.model.response.ResponseWorkbook

interface WorkbookRepository {

    suspend fun getMonthlyRecord(date: String): Result<ResponseWorkbook.ResponseWorkbookData>
}