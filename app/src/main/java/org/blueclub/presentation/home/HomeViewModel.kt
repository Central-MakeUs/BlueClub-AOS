package org.blueclub.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.blueclub.data.datasource.BCDataSource
import org.blueclub.data.model.response.ResponseMonthlyInfo.ResponseMonthlyInfoData
import org.blueclub.domain.repository.WorkbookRepository
import org.blueclub.util.UiState
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val localStorage: BCDataSource,
    private val workbookRepository: WorkbookRepository,
) : ViewModel() {

    private val _homeUiState: MutableStateFlow<UiState<ResponseMonthlyInfoData>> =
        MutableStateFlow(UiState.Loading)
    val homeUiState = _homeUiState.asStateFlow()
    private val _goalIncome = MutableStateFlow("")
    val goalIncome = _goalIncome.asStateFlow()
    private val _progress = MutableStateFlow(0)
    val progress = _progress.asStateFlow()
    private val _totalIncomeString = MutableStateFlow("") // 홈 뷰 달성 수입
    val totalIncomeString = _totalIncomeString.asStateFlow()
    private val _totalIncome = MutableStateFlow(0) // 홈 뷰 달성 수입
    val totalIncome = _totalIncome.asStateFlow()
    private val _totalRecordDay = MutableStateFlow("Hi") // 홈 뷰 달성일
    val totalRecordDay = _totalRecordDay.asStateFlow()
    private val _isRenew = MutableStateFlow(false)
    val isRenew = _isRenew.asStateFlow()
    private val _straightDay = MutableStateFlow(0)
    val straightDay = _straightDay.asStateFlow()
    var nickname = localStorage.nickname
    var job = localStorage.job
    val month = LocalDateTime.now().month.value
    private val _bannerInfo = MutableStateFlow("")
    val bannerInfo = _bannerInfo.asStateFlow()

    val incomeGoal: MutableStateFlow<String?> = MutableStateFlow(null)

    fun getMonthlyInfo() {
        var yearMonth = "${YearMonth.now().year}-"
        if (YearMonth.now().monthValue < 10) {
            yearMonth += "0"
        }
        yearMonth += YearMonth.now().monthValue
        viewModelScope.launch {
            workbookRepository.getMonthlyInfo(yearMonth)
                .onSuccess {
                    val decimalFormat = DecimalFormat("#,###")
                    _homeUiState.value = UiState.Success(it)
                    _goalIncome.value = ((it.targetIncome ?: 0 ) / 10000).toString() + "만원"
                    _progress.value = it.progress ?: 0
                    _totalIncome.value = it.totalIncome ?: 0
                    _totalIncomeString.value = decimalFormat.format(it.totalIncome ?: 0) + "원"
                    _totalRecordDay.value = it.totalDay.toString()
                    localStorage.incomeGoal = it.targetIncome
                    incomeGoal.value = decimalFormat.format(it.targetIncome ?: 0)
                    _isRenew.value = it.isRenew
                    _straightDay.value = it.straightDay
                }
                .onFailure {
                    _homeUiState.value = UiState.Error(it.message)
                }
        }
    }

    fun setBannerInfo(info :String){
        _bannerInfo.value = info
    }

    fun restart(){
        _homeUiState.value = UiState.Loading
        job = localStorage.job
        nickname = localStorage.nickname
    }
}