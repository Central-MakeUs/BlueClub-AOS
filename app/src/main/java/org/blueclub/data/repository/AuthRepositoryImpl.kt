package org.blueclub.data.repository

import org.blueclub.data.datasource.BCDataSource
import org.blueclub.data.datasource.remote.AuthDataSource
import org.blueclub.data.model.request.RequestAuth
import org.blueclub.data.model.response.ResponseAuth
import org.blueclub.domain.repository.AuthRepository
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val localStorage: BCDataSource,
) : AuthRepository {
    override suspend fun login(requestAuth: RequestAuth): Result<ResponseAuth.ResponseAuthData> =
        runCatching {
            authDataSource.login(requestAuth).result
        }.onSuccess {
            with(localStorage) {
                accessToken = it.accessToken
                refreshToken = it.refreshToken

                it.job?.let {
                    isLogin = true
                }
            }
        }.onFailure {
            Timber.e(it.message)
        }


}