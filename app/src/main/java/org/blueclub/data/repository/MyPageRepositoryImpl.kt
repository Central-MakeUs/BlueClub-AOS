package org.blueclub.data.repository

import org.blueclub.data.datasource.remote.MyPageDataSource
import org.blueclub.data.model.response.ResponseNotice
import org.blueclub.domain.repository.MyPageRepository
import timber.log.Timber
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageDataSource: MyPageDataSource,
) : MyPageRepository {

    override suspend fun getNotice(): Result<List<ResponseNotice.ResponseNoticeData>> =
        runCatching {
            myPageDataSource.getNotice().result
        }.onFailure {
            Timber.e(it)
        }

}