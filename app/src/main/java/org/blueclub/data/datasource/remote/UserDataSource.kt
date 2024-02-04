package org.blueclub.data.datasource.remote

import org.blueclub.data.model.request.RequestUserDetails
import org.blueclub.data.service.UserService
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val userService: UserService
) {
    suspend fun writeUserDetails(requestUserDetails: RequestUserDetails) =
        userService.writeUserDetails(requestUserDetails)
}