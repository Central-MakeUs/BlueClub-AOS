package org.blueclub.presentation.workbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import org.blueclub.util.extension.toStateFlow
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

    val incomeGoal: MutableStateFlow<String?> = MutableStateFlow(null)
    val incomeGoalValid : StateFlow<Int?> = incomeGoal.map {
        it?.replace(",", "")?.toInt()
    }.toStateFlow(viewModelScope, 0)

    fun onExpandBtnClick() {
        _isGoalViewExpanded.value = !_isGoalViewExpanded.value
    }

    fun setYearMonth(yearMonth: YearMonth) {
        _yearMonth.value = yearMonth
    }
}