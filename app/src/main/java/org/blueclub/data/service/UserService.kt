package org.blueclub.data.service

import org.blueclub.data.model.request.RequestAgreement
import org.blueclub.data.model.request.RequestUserDetails
import org.blueclub.data.model.response.ResponseBase
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface UserService {

    @POST("user/details")
    suspend fun writeUserDetails(@Body requestUserDetails: RequestUserDetails) : ResponseBase

    @DELETE("user/withdrawal")
    suspend fun deleteAccount() : ResponseBase?

    // 알림 설정 api
    @POST("user/agreement")
    suspend fun setAgreement(@Body requestAgreement: RequestAgreement) : ResponseBase
}