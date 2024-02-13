package org.blueclub.data.service

import org.blueclub.data.model.response.ResponseNotice
import retrofit2.http.GET

interface MyPageService {

    @GET("notice")
    suspend fun getNotice(): ResponseNotice
}