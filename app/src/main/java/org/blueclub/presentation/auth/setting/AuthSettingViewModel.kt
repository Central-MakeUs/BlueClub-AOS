package org.blueclub.presentation.auth.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.blueclub.data.datasource.BCDataSource
import org.blueclub.domain.repository.AuthRepository
import org.blueclub.presentation.type.AuthSettingPageViewType
import org.blueclub.presentation.type.JobSettingViewType
import org.blueclub.presentation.type.NicknameGuideType
import org.blueclub.presentation.type.TosViewType
import org.blueclub.util.extension.toStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthSettingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val localStorage: BCDataSource,
) : ViewModel() {
    private val _selectedJobType: MutableStateFlow<Map<JobSettingViewType, Boolean>> =
        MutableStateFlow(
            mapOf(
                JobSettingViewType.GOLF to false,
                JobSettingViewType.RIDER to false,
                JobSettingViewType.DAY_LABOR to false,
            )
        )
    val selectedJobType = _selectedJobType.asStateFlow()
    private val _chosenJobType: MutableStateFlow<JobSettingViewType> =
        MutableStateFlow(JobSettingViewType.GOLF)
    val chosenJobType = _chosenJobType
    private val _goalSettingFinished: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val goalSettingFinished = _goalSettingFinished.asStateFlow()
    private val _currentPage: MutableStateFlow<AuthSettingPageViewType> =
        MutableStateFlow(AuthSettingPageViewType.JOB)
    val currentPage = _currentPage.asStateFlow()

    val incomeGoal: MutableStateFlow<String?> = MutableStateFlow(null)
    val incomeGoalValid: StateFlow<Int?> = incomeGoal.map {
        it?.replace(",", "")?.toInt()
    }.toStateFlow(viewModelScope, 0)

    val nickname = MutableStateFlow(localStorage.nickname)
    private val _isNicknameCorrect = MutableLiveData(true)
    val isNicknameCorrect: LiveData<Boolean> get() = _isNicknameCorrect
    private val _isNicknameAvailable: MutableLiveData<Boolean?> =
        MutableLiveData(true) // 중복인지 확인하는 변수 : null(아무 값 안보임), false (중복), true(생성 가능)
    val isNicknameAvailable: LiveData<Boolean?> get() = _isNicknameAvailable
    private val _nicknameInputGuide: MutableLiveData<NicknameGuideType> =
        MutableLiveData(NicknameGuideType.VALID_NICKNAME)
    val nicknameInputGuide: LiveData<NicknameGuideType> get() = _nicknameInputGuide

    private val _selectedTosType: MutableStateFlow<Map<TosViewType, Boolean>> =
        MutableStateFlow(
            mapOf(
                TosViewType.AGE to false,
                TosViewType.SERVICE to false,
                TosViewType.PRIVACY to false,
                TosViewType.MARKETING to false,
                TosViewType.EVENT to false,
            )
        )
    val selectedTosType = _selectedTosType.asStateFlow()

    init {
        _isNicknameCorrect.value = true
        _isNicknameAvailable.value = true
        _nicknameInputGuide.value = NicknameGuideType.VALID_NICKNAME
    }

    fun setChosenJobType(jobType: JobSettingViewType) {
        _chosenJobType.value = jobType
    }

    fun isGoalSettingFinished(selected: Boolean) {
        _goalSettingFinished.value = selected
    }

    fun setCurrentPage(position: Int) {
        AuthSettingPageViewType.entries.map {
            if (it.ordinal == position)
                _currentPage.value = it
        }
    }

    fun setJobType(jobType: JobSettingViewType) {
        val isSelected = selectedJobType.value[jobType] ?: return
        _selectedJobType.value =
            mutableMapOf(
                JobSettingViewType.GOLF to false,
                JobSettingViewType.RIDER to false,
                JobSettingViewType.DAY_LABOR to false,
            ).apply {
                this[jobType] = !isSelected
            }
    }

    fun setTosType(tosType: TosViewType) {
        val isSelected = selectedTosType.value[tosType] ?: return
        _selectedTosType.value = selectedTosType.value.toMutableMap().apply {
            this[tosType] = !isSelected
        }
    }

    fun setWholeTosType(): Boolean {
        val isSelected = selectedTosType.value.values.contains(true)
        _selectedTosType.value = mutableMapOf(
            TosViewType.AGE to isSelected,
            TosViewType.SERVICE to isSelected,
            TosViewType.PRIVACY to isSelected,
            TosViewType.MARKETING to isSelected,
            TosViewType.EVENT to isSelected,
        )
        return isSelected
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

}