package org.blueclub.data.repository

import org.blueclub.data.datasource.BCDataSource
import org.blueclub.data.datasource.remote.AuthDataSource
import org.blueclub.data.model.request.RequestAuth
import org.blueclub.data.model.response.ResponseAuth
import org.blueclub.data.model.response.ResponseBase
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
                this.accessToken = it.accessToken
                this.refreshToken = it.refreshToken
                this.nickname = it.nickname
                this.job = it.job
                it.job?.let {
                    isLogin = true
                }
            }
        }.onFailure {
            Timber.e(it.message)
        }

    override suspend fun checkDuplication(nickname: String): Result<ResponseBase> =
        runCatching {
            authDataSource.checkNicknameDuplication(nickname)
        }.onSuccess {
        }.onFailure {
            Timber.e(it.message)
        }


}