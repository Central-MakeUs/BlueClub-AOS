package org.blueclub.domain.repository

import org.blueclub.data.model.request.RequestAuth
import org.blueclub.data.model.response.ResponseAuth

interface AuthRepository {

    suspend fun login(requestAuth: RequestAuth): Result<ResponseAuth.ResponseAuthData>
}