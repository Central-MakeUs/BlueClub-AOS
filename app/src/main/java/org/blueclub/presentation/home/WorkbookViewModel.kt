package org.blueclub.presentation.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WorkbookViewModel : ViewModel() {
    private val _isGoalViewExpanded = MutableStateFlow(false)
    val isGoalViewExpanded = _isGoalViewExpanded.asStateFlow()
    private val _goalProgress = MutableStateFlow(3)
    val goalProgress = _goalProgress.asStateFlow()


    fun onExpandBtnClick() {
        _isGoalViewExpanded.value = !_isGoalViewExpanded.value
    }
}