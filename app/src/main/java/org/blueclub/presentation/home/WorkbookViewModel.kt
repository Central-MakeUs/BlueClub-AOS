package org.blueclub.presentation.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.YearMonth

class WorkbookViewModel : ViewModel() {
    private val _isGoalViewExpanded = MutableStateFlow(false)
    val isGoalViewExpanded = _isGoalViewExpanded.asStateFlow()
    private val _goalProgress = MutableStateFlow(70)
    val goalProgress = _goalProgress.asStateFlow()
    private val _yearMonth = MutableStateFlow(YearMonth.now())
    val yearMonth = _yearMonth.asStateFlow()
    private val _today = MutableStateFlow(YearMonth.now())
    val today = _today.asStateFlow()


    fun onExpandBtnClick() {
        _isGoalViewExpanded.value = !_isGoalViewExpanded.value
    }

    fun setYearMonth(yearMonth: YearMonth) {
        _yearMonth.value = yearMonth
    }
}