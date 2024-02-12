package org.blueclub.data.datasource.remote

import org.blueclub.data.model.request.RequestAgreement
import org.blueclub.data.model.request.RequestUserDetails
import org.blueclub.data.model.response.ResponseBase
import org.blueclub.data.service.UserService
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val userService: UserService
) {
    suspend fun writeUserDetails(requestUserDetails: RequestUserDetails) =
        userService.writeUserDetails(requestUserDetails)

    suspend fun deleteAccount(): ResponseBase = userService.deleteAccount()

    suspend fun setAgreement(requestAgreement: RequestAgreement) =
        userService.setAgreement(requestAgreement)
}