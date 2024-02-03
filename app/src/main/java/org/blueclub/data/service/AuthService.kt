package org.blueclub.data.service

import org.blueclub.data.model.request.RequestAuth
import org.blueclub.data.model.response.ResponseAuth
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth")
    suspend fun login(@Body authInfo: RequestAuth): ResponseAuth

}