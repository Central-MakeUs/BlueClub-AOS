package org.blueclub.presentation.auth.setting

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.blueclub.presentation.type.AuthSettingPageViewType
import org.blueclub.presentation.type.JobSettingViewType

class AuthSettingViewModel : ViewModel() {
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
    private val _selectedYear: MutableStateFlow<String?> = MutableStateFlow(null)
    val selectedYear = _selectedYear.asStateFlow()
    private val _yearSelectFinished: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val yearSelectFinished = _yearSelectFinished.asStateFlow()
    private val _currentPage: MutableStateFlow<AuthSettingPageViewType> =
        MutableStateFlow(AuthSettingPageViewType.JOB)
    val currentPage = _currentPage.asStateFlow()

    val nickname = MutableStateFlow("")
    private val _isNicknameCorrect = MutableStateFlow(false)
    val isNicknameCorrect = _isNicknameCorrect.asStateFlow()
    // TODO 닉네임 중복확인 후 버튼 활성화하는 작업 필요

    fun setChosenJobType(jobType: JobSettingViewType) {
        _chosenJobType.value = jobType
    }

    fun setSelectedYear(selectedYear: String?) {
        _selectedYear.value = selectedYear
    }

    fun setYearSelected(selected: Boolean) {
        _yearSelectFinished.value = selected
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
}