package org.blueclub.data.datasource

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import org.blueclub.BuildConfig
import org.blueclub.presentation.type.LoginPlatformType
import org.blueclub.util.safeValueOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BCDataSource @Inject constructor(@ApplicationContext context: Context) {
    private val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val dataStore: SharedPreferences =
        if (BuildConfig.DEBUG) context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        else EncryptedSharedPreferences.create(
            context,
            FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    var accessToken: String
        set(value) = dataStore.edit { putString(ACCESS_TOKEN, value) }
        get() = dataStore.getString(
            ACCESS_TOKEN,
            ""
        ) ?: ""

    var refreshToken: String
        set(value) = dataStore.edit { putString(REFRESH_TOKEN, value) }
        get() = dataStore.getString(
            REFRESH_TOKEN,
            ""
        ) ?: ""

    var isLogin: Boolean
        set(value) = dataStore.edit { putBoolean(IS_LOGIN, value) }
        get() = dataStore.getBoolean(IS_LOGIN, false)

    var loginPlatform: LoginPlatformType
        set(value) = dataStore.edit { putString(LOGIN_PLATFORM, value.name) }
        get() = safeValueOf<LoginPlatformType>(dataStore.getString(LOGIN_PLATFORM, ""))
            ?: LoginPlatformType.NONE

    var nickname: String?
        set(value) = dataStore.edit { putString(NICKNAME, value) }
        get() = dataStore.getString(NICKNAME, "")

    var job: String?
        set(value) = dataStore.edit { putString(JOB, value) }
        get() = dataStore.getString(JOB, "")

    var incomeGoal: Int?
        set(value) = dataStore.edit { putInt(INCOME, value ?: 100000) }
        get() = dataStore.getInt(INCOME, 100000)

    fun clear(isWithdrawal: Boolean = false) {
        dataStore.edit {
            if (isWithdrawal) {
                clear()
            } else {
                remove(ACCESS_TOKEN)
                remove(REFRESH_TOKEN)
                remove(IS_LOGIN)
                remove(LOGIN_PLATFORM)
                remove(NICKNAME)
            }
        }
    }

    companion object {
        const val FILE_NAME = "signSharedPreferences"
        const val ACCESS_TOKEN = "accessToken"
        const val REFRESH_TOKEN = "refreshToken"
        const val LOGIN_PLATFORM = "loginPlatform"
        const val IS_LOGIN = "isLogin"
        const val NICKNAME = "nickname"
        const val JOB = "job"
        const val INCOME = "income"
    }
}