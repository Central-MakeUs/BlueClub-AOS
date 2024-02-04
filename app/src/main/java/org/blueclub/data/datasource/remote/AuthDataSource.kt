package org.blueclub.data.datasource.remote

import org.blueclub.data.model.request.RequestAuth
import org.blueclub.data.model.response.ResponseAuth
import org.blueclub.data.service.AuthService
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val authService: AuthService,
) {
    suspend fun login(requestAuth: RequestAuth): ResponseAuth = authService.login(requestAuth)

    suspend fun checkNicknameDuplication(nickname: String) =
        authService.checkDuplication(nickname)
}