package org.blueclub.domain.repository

import org.blueclub.data.model.request.RequestUserDetails
import org.blueclub.data.model.response.ResponseBase

interface UserRepository {
    suspend fun writeUserDetails(requestUserDetails: RequestUserDetails) : Result<ResponseBase>
}