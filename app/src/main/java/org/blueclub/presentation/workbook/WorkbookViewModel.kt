package org.blueclub.presentation.workbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.blueclub.data.datasource.BCDataSource
import org.blueclub.data.model.request.RequestGoalSetting
import org.blueclub.data.model.response.ResponseMonthlyInfo
import org.blueclub.domain.model.DailyWorkInfo
import org.blueclub.domain.repository.WorkbookRepository
import org.blueclub.presentation.type.GoalErrorType
import org.blueclub.util.UiState
import org.blueclub.util.extension.toStateFlow
import timber.log.Timber
import java.text.DecimalFormat
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class WorkbookViewModel @Inject constructor(
    private val workbookRepository: WorkbookRepository,
    private val localStorage: BCDataSource,
) : ViewModel() {
    private val _monthlyRecordUiState: MutableStateFlow<UiState<List<DailyWorkInfo>>> =
        MutableStateFlow(UiState.Loading)
    val monthlyRecordUiState = _monthlyRecordUiState.asStateFlow()
    private val _workData: MutableStateFlow<Map<Int, DailyWorkInfo>> = MutableStateFlow(mapOf())
    val workData = _workData.asStateFlow()
    private val _isGoalViewExpanded = MutableStateFlow(false)
    val isGoalViewExpanded = _isGoalViewExpanded.asStateFlow()
    private val _yearMonth = MutableStateFlow(YearMonth.now())
    val yearMonth = _yearMonth.asStateFlow()
    private val _today = MutableStateFlow(YearMonth.now())
    val today = _today.asStateFlow()

    val job = localStorage.job

    // 목표 설정 바텀시트 관련
    val incomeGoal: MutableStateFlow<String?> = MutableStateFlow(null)
    val incomeGoalValid: StateFlow<Int?> = incomeGoal.map {
        it?.replace(",", "")?.toIntOrNull() ?: 0
    }.toStateFlow(viewModelScope, 0)
    val goalErrorMsg: StateFlow<GoalErrorType> = incomeGoalValid.map{
        val income = it ?: 0
        if(income <100000)
            GoalErrorType.TOO_LOW
        else if(income > 99999999)
            GoalErrorType.TOO_HIGH
        else
            GoalErrorType.CORRECT
    }.toStateFlow(viewModelScope, GoalErrorType.TOO_LOW)
    private val _goalSettingUiState: MutableStateFlow<UiState<Boolean>> =
        MutableStateFlow(UiState.Loading)
    val goalSettingUiState = _goalSettingUiState.asStateFlow()

    // 달성 수입 관련
    private val _monthlyInfoUiState: MutableStateFlow<UiState<ResponseMonthlyInfo.ResponseMonthlyInfoData>> =
        MutableStateFlow(UiState.Loading)
    val monthlyInfoUiState = _monthlyInfoUiState.asStateFlow()
    private val _goalIncome = MutableStateFlow("")
    val goalIncome = _goalIncome.asStateFlow()
    private val _progress = MutableStateFlow(0)
    val progress = _progress.asStateFlow()
    private val _totalIncomeString = MutableStateFlow("") // 홈 뷰 달성 수입
    val totalIncomeString = _totalIncomeString.asStateFlow()
    private val _totalIncome = MutableStateFlow(0) // 홈 뷰 달성 수입
    val totalIncome = _totalIncome.asStateFlow()
    private val _totalRecordDay = MutableStateFlow(-1) // 홈 뷰 달성일
    val totalRecordDay = _totalRecordDay.asStateFlow()

    fun onExpandBtnClick() {
        _isGoalViewExpanded.value = !_isGoalViewExpanded.value
    }

    fun setYearMonth(yearMonth: YearMonth) {
        _yearMonth.value = yearMonth
    }

    fun setMonthlyRecordUiState(uiState: UiState<List<DailyWorkInfo>>) {
        _monthlyRecordUiState.value = uiState
    }

    fun setMonthlyInfoUiState(uiState: UiState<ResponseMonthlyInfo.ResponseMonthlyInfoData>) {
        _monthlyInfoUiState.value = uiState
    }

    fun setWorkData(workData: Map<Int, DailyWorkInfo>) {
        _workData.value = workData
    }

    fun getMonthlyRecord(yearMonth: String) {
        viewModelScope.launch {
            workbookRepository.getMonthlyRecord(yearMonth)
                .onSuccess { responseWorkbookData ->
                    _monthlyRecordUiState.value =
                        UiState.Success(
                            responseWorkbookData.monthlyRecord
                                .map { it.toDailyWorkData() }
                        )
                    _workData.value = responseWorkbookData.monthlyRecord
                        .map { it.toDailyWorkData() }
                        .map { it.day to it }.toMap()
                }
                .onFailure {
                    _monthlyRecordUiState.value = UiState.Error(it.message)
                }
        }
    }

    fun getMonthlyInfo() {
        var date = "${yearMonth.value.year}-"
        if (yearMonth.value.monthValue < 10) {
            date += "0"
        }
        date += yearMonth.value.monthValue
        viewModelScope.launch {
            workbookRepository.getMonthlyInfo(date)
                .onSuccess {
                    val decimalFormat = DecimalFormat("#,###")
                    _monthlyInfoUiState.value = UiState.Success(it)
                    _goalIncome.value = ((it.targetIncome ?: 0) / 10000).toString() + "만원"
                    _progress.value = it.progress ?: 0
                    _totalIncome.value = it.totalIncome ?: 0
                    _totalIncomeString.value = decimalFormat.format(it.totalIncome ?: 0) + "원"
                    _totalRecordDay.value = it.totalDay
                    incomeGoal.value = decimalFormat.format(it.targetIncome ?: 0)
                    _goalSettingUiState.value = UiState.Loading
                }
                .onFailure {
                    _monthlyInfoUiState.value = UiState.Error(it.message)
                }
        }
    }

    fun uploadMonthlyGoal() {
        var date = "${yearMonth.value.year}-"
        if (yearMonth.value.monthValue < 10) {
            date += "0"
        }
        date += yearMonth.value.monthValue
        viewModelScope.launch {
            workbookRepository.uploadMonthlyGoal(
                RequestGoalSetting(
                    date, incomeGoal.value?.replace(",", "")?.toIntOrNull() ?: 100000
                )
            ).onSuccess {
                _goalSettingUiState.value = UiState.Success(true)
                _monthlyInfoUiState.value = UiState.Loading
            }.onFailure {
                Timber.d(it.message)
            }
        }
    }
}