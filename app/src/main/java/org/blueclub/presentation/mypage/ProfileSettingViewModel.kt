package org.blueclub.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import org.blueclub.data.datasource.BCDataSource
import org.blueclub.presentation.type.JobSettingViewType
import org.blueclub.util.UiState
import org.blueclub.util.extension.toStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileSettingViewModel @Inject constructor(
    private val localStorage: BCDataSource,
) : ViewModel() {
    val nickname = MutableStateFlow("")
    private val _isNicknameCorrect = MutableStateFlow(false)
    val isNicknameCorrect = _isNicknameCorrect.asStateFlow()

    private val _chosenJobType: MutableStateFlow<JobSettingViewType> =
        MutableStateFlow(JobSettingViewType.GOLF)
    val chosenJobType = _chosenJobType

    val incomeGoal: MutableStateFlow<String?> = MutableStateFlow(null)
    val incomeGoalValid : StateFlow<Int?> = incomeGoal.map {
        it?.replace(",", "")?.toInt()
    }.toStateFlow(viewModelScope, 0)

    private val _logoutUiState: MutableStateFlow<UiState<Boolean>> = MutableStateFlow(UiState.Loading)
    val logoutUiState = _logoutUiState.asStateFlow()
    val loginPlatForm = localStorage.loginPlatform

    fun logout() {
        localStorage.clear()
        _logoutUiState.value = UiState.Success(true)
    }
}