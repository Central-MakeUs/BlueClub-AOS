package org.blueclub.data.datasource.remote

import org.blueclub.data.model.response.ResponseNotice
import org.blueclub.data.service.MyPageService
import javax.inject.Inject

class MyPageDataSource @Inject constructor(
    private val myPageService: MyPageService,
) {
    suspend fun getNotice(): ResponseNotice = myPageService.getNotice()
}