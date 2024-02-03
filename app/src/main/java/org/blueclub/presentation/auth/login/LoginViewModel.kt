package org.blueclub.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.blueclub.data.datasource.BCDataSource
import org.blueclub.data.model.request.RequestAuth
import org.blueclub.domain.repository.AuthRepository
import org.blueclub.domain.type.SignType
import org.blueclub.presentation.type.LoginPlatformType
import org.blueclub.util.UiState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val localStorage: BCDataSource,
) : ViewModel() {
    private val _loginUiState = MutableStateFlow<UiState<SignType>>(UiState.Loading)
    val loginUiState get() = _loginUiState.asStateFlow()

    fun login(
        loginPlatform: LoginPlatformType,
        accessToken: String,
        email: String,
        nickname: String,
        profileImageUrl: String,
    ) {
        localStorage.loginPlatform = loginPlatform
        //localStorage.accessToken = accessToken
        viewModelScope.launch {
            authRepository.login(
                RequestAuth(
                    accessToken,
                    loginPlatform.name,
                    nickname,
                    nickname,
                    email,
                    "",
                    profileImageUrl,
                    null,
                )
            ).onSuccess {
                if (it.job == null)
                    _loginUiState.value = UiState.Success(SignType.SIGN_UP)
                else
                    _loginUiState.value = UiState.Success(SignType.SIGN_IN)
            }.onFailure {
                _loginUiState.value = UiState.Error(it.message)
            }
        }

    }
}