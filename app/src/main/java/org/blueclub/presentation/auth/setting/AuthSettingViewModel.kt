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
                JobSettingViewType.DELIVERY to false,
                JobSettingViewType.INSURANCE to false,
                JobSettingViewType.FREELANCER to false,
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

    fun setChosenJobType(jobType: JobSettingViewType) {
        _chosenJobType.value = jobType
    }

    fun setSelectedYear(selectedYear: String?) {
        _selectedYear.value = selectedYear
    }

    fun setYearSelected() {
        _yearSelectFinished.value = true
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
                JobSettingViewType.DELIVERY to false,
                JobSettingViewType.INSURANCE to false,
                JobSettingViewType.FREELANCER to false,
            ).apply {
                this[jobType] = !isSelected
            }
    }
}