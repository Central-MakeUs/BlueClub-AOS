package org.blueclub.data.repository

import org.blueclub.data.datasource.BCDataSource
import org.blueclub.data.datasource.remote.UserDataSource
import org.blueclub.data.model.request.RequestAgreement
import org.blueclub.data.model.request.RequestUserDetails
import org.blueclub.data.model.response.ResponseBase
import org.blueclub.domain.repository.UserRepository
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val localStorage: BCDataSource,
) : UserRepository {

    override suspend fun writeUserDetails(requestUserDetails: RequestUserDetails): Result<ResponseBase> =
        runCatching {
            userDataSource.writeUserDetails(requestUserDetails)
        }.onSuccess {
            with(localStorage) {
                this.nickname = requestUserDetails.nickname
                this.job = requestUserDetails.job
            }
        }.onFailure {
            Timber.e(it.message)
        }

    override suspend fun deleteAccount(): Result<ResponseBase?> =
        runCatching {
            userDataSource.deleteAccount()
        }.onSuccess {
            localStorage.clear(true)
        }.onFailure {
            Timber.e(it.message)
        }

    override suspend fun setAgreement(requestAgreement: RequestAgreement): Result<ResponseBase> =
        runCatching {
            userDataSource.setAgreement(requestAgreement)
        }.onFailure {
            Timber.e(it.message)
        }
}