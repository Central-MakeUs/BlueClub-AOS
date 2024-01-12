package org.blueclub.presentation.auth.setting

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    fun setJobType(jobType: JobSettingViewType) {
        val isSelected = selectedJobType.value[jobType] ?: return
        _selectedJobType.value = _selectedJobType.value.toMutableMap().apply {
            this[jobType] = !isSelected
        }
    }
}