package org.blueclub.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import org.blueclub.presentation.type.JobSettingViewType
import org.blueclub.util.extension.toStateFlow

class ProfileSettingViewModel : ViewModel() {
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

}