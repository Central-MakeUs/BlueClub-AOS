package org.blueclub.data.service

import org.blueclub.data.model.request.RequestAuth
import org.blueclub.data.model.response.ResponseAuth
import org.blueclub.data.model.response.ResponseBase
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("auth")
    suspend fun login(@Body authInfo: RequestAuth): ResponseAuth

    @GET("auth/duplicated")
    suspend fun checkDuplication(@Query("nickname") nickname: String): ResponseBase
}