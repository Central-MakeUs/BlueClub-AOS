package org.blueclub.data.interceptor

import android.app.Application
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.blueclub.data.datasource.BCDataSource
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val localStorage: BCDataSource,
    private val gson: Gson,
    private val context: Application,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authRequest = originalRequest.newAuthBuilder().build()
        val response = chain.proceed(authRequest)

        when (response.code) {
            403 -> {
                // TODO
            }
        }

        return response
    }

    private fun Request.newAuthBuilder() =
        this.newBuilder()
            .addHeader(ACCESS_TOKEN, "Bearer " + localStorage.accessToken)
            .addHeader(REFRESH_TOKEN, "Bearer " + localStorage.refreshToken)


    companion object {
        private const val ACCESS_TOKEN = "Authorization"
        private const val REFRESH_TOKEN = "Authorization-refresh"
    }
}