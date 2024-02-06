package org.blueclub.domain.repository

import org.blueclub.data.model.response.ResponseNotice

interface MyPageRepository {
    suspend fun getNotice(): Result<List<ResponseNotice.ResponseNoticeData>>
}