package org.blueclub.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.blueclub.data.datasource.BCDataSource
import org.blueclub.domain.repository.UserRepository
import org.blueclub.presentation.type.JobSettingViewType
import org.blueclub.util.UiState
import org.blueclub.util.extension.toStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileSettingViewModel @Inject constructor(
    private val localStorage: BCDataSource,
    private val userRepository: UserRepository,
) : ViewModel() {
    val nickname = MutableStateFlow(localStorage.nickname)
    private val _isNicknameCorrect = MutableStateFlow(false)
    val isNicknameCorrect = _isNicknameCorrect.asStateFlow()

    private val _chosenJobType: MutableStateFlow<JobSettingViewType> =
        MutableStateFlow(JobSettingViewType.GOLF)
    val chosenJobType = _chosenJobType

    val incomeGoal: MutableStateFlow<String?> = MutableStateFlow(null)
    val incomeGoalValid: StateFlow<Int?> = incomeGoal.map {
        it?.replace(",", "")?.toInt()
    }.toStateFlow(viewModelScope, 0)

    private val _logoutUiState: MutableStateFlow<UiState<Boolean>> =
        MutableStateFlow(UiState.Loading)
    val logoutUiState = _logoutUiState.asStateFlow()
    private val _deleteAccountUiState: MutableStateFlow<UiState<Boolean>> =
        MutableStateFlow(UiState.Loading)
    val deleteAccountUiState = _deleteAccountUiState.asStateFlow()
    val loginPlatForm = localStorage.loginPlatform

    fun logout() {
        localStorage.clear()
        _logoutUiState.value = UiState.Success(true)
    }

    fun deleteAccount() {
        viewModelScope.launch {
            userRepository.deleteAccount().onSuccess {
                _deleteAccountUiState.value = UiState.Success(true)
            }.onFailure {
                _deleteAccountUiState.value = UiState.Error(it.message)
            }
        }
    }
}