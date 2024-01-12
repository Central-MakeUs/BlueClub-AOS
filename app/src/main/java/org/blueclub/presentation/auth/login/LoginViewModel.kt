package org.blueclub.presentation.auth.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.blueclub.data.datasource.BCDataSource
import org.blueclub.presentation.type.LoginPlatformType
import org.blueclub.util.UiState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val localStorage: BCDataSource,
) : ViewModel() {
    private val _loginUiState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val loginUiState get() = _loginUiState.asStateFlow()

    fun login(loginPlatform: LoginPlatformType, accessToken: String) {
        localStorage.loginPlatform = loginPlatform
        localStorage.accessToken = accessToken
        // TODO 서버 연결 필요
        _loginUiState.value = UiState.Success(true)
    }
}