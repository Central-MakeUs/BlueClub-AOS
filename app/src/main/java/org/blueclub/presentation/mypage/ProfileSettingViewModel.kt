package org.blueclub.presentation.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.blueclub.data.datasource.BCDataSource
import org.blueclub.data.model.request.RequestModifyUserDetails
import org.blueclub.domain.repository.AuthRepository
import org.blueclub.domain.repository.UserRepository
import org.blueclub.presentation.type.JobSettingViewType
import org.blueclub.presentation.type.NicknameGuideType
import org.blueclub.util.UiState
import org.blueclub.util.extension.toStateFlow
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class ProfileSettingViewModel @Inject constructor(
    private val localStorage: BCDataSource,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    val nickname: MutableStateFlow<String?> = MutableStateFlow(localStorage.nickname)
    private val _isNicknameCorrect = MutableLiveData(true)
    val isNicknameCorrect: LiveData<Boolean> get() = _isNicknameCorrect
    private val _isNicknameAvailable: MutableLiveData<Boolean?> =
        MutableLiveData(true) // 중복인지 확인하는 변수 : null(아무 값 안보임), false (중복), true(생성 가능)
    val isNicknameAvailable: LiveData<Boolean?> get() = _isNicknameAvailable
    private val _nicknameInputGuide: MutableLiveData<NicknameGuideType> =
        MutableLiveData(NicknameGuideType.VALID_NICKNAME)
    val nicknameInputGuide: LiveData<NicknameGuideType> get() = _nicknameInputGuide

    private val _chosenJobType: MutableStateFlow<JobSettingViewType> =
        MutableStateFlow(JobSettingViewType.GOLF)
    val chosenJobType = _chosenJobType

    val incomeGoal: MutableStateFlow<String?> =
        MutableStateFlow(DecimalFormat("#,###").format(localStorage.incomeGoal))
    val incomeGoalValid: StateFlow<Int?> = incomeGoal.map {
        it?.replace(",", "")?.toInt() ?: 100000
    }.toStateFlow(viewModelScope, 0)

    val isSaveAvailable = combine(
        _isNicknameAvailable.asFlow(),
        incomeGoalValid
    ) { nicknameAvailable, income, ->
        nicknameAvailable == true && income != null && income >= 100000 && income <= 99999999
    }.asLiveData()

    private val _logoutUiState: MutableStateFlow<UiState<Boolean>> =
        MutableStateFlow(UiState.Loading)
    val logoutUiState = _logoutUiState.asStateFlow()
    private val _deleteAccountUiState: MutableStateFlow<UiState<Boolean>> =
        MutableStateFlow(UiState.Loading)
    val deleteAccountUiState = _deleteAccountUiState.asStateFlow()
    private val _modifiedAccountUiState: MutableStateFlow<UiState<Boolean>> =
        MutableStateFlow(UiState.Loading)
    val modifiedAccountUiState = _modifiedAccountUiState.asStateFlow()
    val loginPlatForm = localStorage.loginPlatform

    fun modifyUserDetails() {
        viewModelScope.launch {
            if(nickname.value != null){
                userRepository.modifyUserDetails(
                    RequestModifyUserDetails(
                        nickname.value!!,
                        chosenJobType.value.title,
                        //"골프캐디",
                        incomeGoal.value?.replace(",", "")?.toIntOrNull() ?: 100000
                    )
                ).onSuccess {
                    _modifiedAccountUiState.value = UiState.Success(true)
                    localStorage.job=chosenJobType.value.title
                }.onFailure {
                    _modifiedAccountUiState.value = UiState.Error(it.message)
                }
            }

        }
    }

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

    fun checkNicknameDuplication(nickname: String) {
        viewModelScope.launch {
            authRepository.checkDuplication(nickname)
                .onSuccess {
                    _isNicknameAvailable.value = true
                    _nicknameInputGuide.value = NicknameGuideType.VALID_NICKNAME
                }
                .onFailure {
                    _isNicknameAvailable.value = false
                    _nicknameInputGuide.value = NicknameGuideType.ALREADY_USED
                }
        }
    }

    fun setNicknameCorrect(isCorrect: Boolean) {
        _isNicknameCorrect.value = isCorrect
    }

    fun setNicknameAvailable(isAvailable: Boolean?) {
        _isNicknameAvailable.value = isAvailable
    }

    fun setNicknameInputGuide(inputGuideType: NicknameGuideType) {
        _nicknameInputGuide.value = inputGuideType
    }

    fun setJobType(jobType: JobSettingViewType) {
        _chosenJobType.value = jobType
    }
}