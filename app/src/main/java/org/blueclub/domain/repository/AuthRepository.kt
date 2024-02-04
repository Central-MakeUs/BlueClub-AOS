package org.blueclub.domain.repository

import org.blueclub.data.model.request.RequestAuth
import org.blueclub.data.model.response.ResponseAuth
import org.blueclub.data.model.response.ResponseBase

interface AuthRepository {

    suspend fun login(requestAuth: RequestAuth): Result<ResponseAuth.ResponseAuthData>

    suspend fun checkDuplication(nickname: String): Result<ResponseBase>
}