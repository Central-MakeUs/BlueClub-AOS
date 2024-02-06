package org.blueclub.presentation.workbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.blueclub.domain.model.DailyWorkInfo
import org.blueclub.domain.repository.WorkbookRepository
import org.blueclub.util.UiState
import org.blueclub.util.extension.toStateFlow
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class WorkbookViewModel @Inject constructor(
    private val workbookRepository: WorkbookRepository,
) : ViewModel() {
    private val _monthlyRecordUiState: MutableStateFlow<UiState<List<DailyWorkInfo>>> =
        MutableStateFlow(UiState.Loading)
    val monthlyRecordUiState = _monthlyRecordUiState.asStateFlow()
    private val _isGoalViewExpanded = MutableStateFlow(false)
    val isGoalViewExpanded = _isGoalViewExpanded.asStateFlow()
    private val _goalProgress = MutableStateFlow(70)
    val goalProgress = _goalProgress.asStateFlow()
    private val _yearMonth = MutableStateFlow(YearMonth.now())
    val yearMonth = _yearMonth.asStateFlow()
    private val _today = MutableStateFlow(YearMonth.now())
    val today = _today.asStateFlow()

    val incomeGoal: MutableStateFlow<String?> = MutableStateFlow(null)
    val incomeGoalValid: StateFlow<Int?> = incomeGoal.map {
        it?.replace(",", "")?.toInt()
    }.toStateFlow(viewModelScope, 0)

    fun onExpandBtnClick() {
        _isGoalViewExpanded.value = !_isGoalViewExpanded.value
    }

    fun setYearMonth(yearMonth: YearMonth) {
        _yearMonth.value = yearMonth
    }

    fun getMonthlyRecord() {
        var yearMonth = "${YearMonth.now().year}-"
        if (YearMonth.now().monthValue < 10) {
            yearMonth += "0"
        }
        yearMonth += YearMonth.now().monthValue
        viewModelScope.launch {
            workbookRepository.getMonthlyRecord(yearMonth)
                .onSuccess { responseWorkbookData ->
                    _monthlyRecordUiState.value =
                        UiState.Success(responseWorkbookData.monthlyRecord.map { it.toDailyWorkData() })
                }
                .onFailure {
                    _monthlyRecordUiState.value = UiState.Error(it.message)
                }
        }
    }
}