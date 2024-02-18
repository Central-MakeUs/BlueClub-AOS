package org.blueclub.presentation.daily.daylabor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.blueclub.data.model.request.RequestDayLaborDiary
import org.blueclub.domain.repository.WorkbookRepository
import org.blueclub.presentation.type.DailyWorkType
import org.blueclub.util.UiState
import org.blueclub.util.getDayOfWeek
import timber.log.Timber
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class WorkDetailDayLaborViewModel @Inject constructor(
    private val workbookRepository: WorkbookRepository,
) : ViewModel() {

    private val _month = MutableStateFlow(1)
    val month = _month.asStateFlow()
    private val _day = MutableStateFlow(1)
    val day = _day.asStateFlow()
    private val _date = MutableStateFlow("")
    val date = _date.asStateFlow()
    private val _dow = MutableStateFlow("") // 요일
    val dow = _dow.asStateFlow()

    private val _workId: MutableStateFlow<Int?> = MutableStateFlow(null)
    val workId = _workId.asStateFlow()

    private val _workType = MutableStateFlow(DailyWorkType.DEFAULT) // 근무 형태(필수)
    val workType = _workType.asStateFlow()
    val dayLaborName: MutableStateFlow<String?> = MutableStateFlow(null) // 현장명(필수)
    val dayLaborIncome: MutableStateFlow<String?> = MutableStateFlow(null) // 일당(필수)
    val dayLaborType: MutableStateFlow<String?> = MutableStateFlow(null) // 직종
    private val _manHour = MutableStateFlow(0.0) // 공수
    val manHour = _manHour.asStateFlow()

    private val _memo = MutableStateFlow("")
    val memo = _memo.asStateFlow()
    private val _income = MutableStateFlow("계산 중이에요")
    val income = _income.asStateFlow()
    val spentAmount: MutableStateFlow<String?> = MutableStateFlow(null)
    val savingsAmount: MutableStateFlow<String?> = MutableStateFlow(null)

    private val _incomePercentage = MutableStateFlow("N")
    val incomePercentage = _incomePercentage.asStateFlow()
    val incomeGoal = MutableStateFlow(0)

    private val _isUploadedUiState: MutableStateFlow<UiState<Boolean>> =
        MutableStateFlow(UiState.Loading)
    val isUploadedUiState = _isUploadedUiState.asStateFlow()

    val isInputCompleted = combine(
        _workType,
        dayLaborName,
        dayLaborIncome
    ) { workType, dayLaborName, dayLaborIncome ->
        workType != DailyWorkType.REST && workType != DailyWorkType.DEFAULT
                && !dayLaborName.isNullOrBlank()
                && (dayLaborIncome?.replace(",", "")?.toIntOrNull() ?: 0) > 0
    }.asLiveData()

    val isSaveAvailable = combine(
        _workType,
        dayLaborName,
        dayLaborIncome
    ) { workType, dayLaborName, dayLaborIncome ->
        workType == DailyWorkType.REST || (workType != DailyWorkType.DEFAULT
                && !dayLaborName.isNullOrBlank()
                && (dayLaborIncome?.replace(",", "")?.toIntOrNull() ?: 0) > 0)
    }.asLiveData()


    fun setDate(date: String) {
        _date.value = date
        _month.value = date.slice(5..6).toIntOrNull() ?: 0
        _day.value = date.slice(8..9).toIntOrNull() ?: 0
        _dow.value = getDayOfWeek(date)
    }

    fun setWorkType(workType: DailyWorkType) {
        _workType.value = workType
    }

    fun minusManHourCount() {
        if (_manHour.value > 0.0)
            _manHour.value = DecimalFormat("#.#").format(manHour.value - 0.1).toDouble()
    }

    fun plusManHourCount() {
        if (_manHour.value < 3.0)
            _manHour.value = DecimalFormat("#.#").format(manHour.value + 0.1).toDouble()
    }

    fun calculateIncome() {
        val decimalFormat = DecimalFormat("#,###")
        val income = (dayLaborIncome.value?.replace(",", "")?.toIntOrNull() ?: 0)
        if (income == 0)
            _income.value = "계산 중이에요"
        else
            _income.value = decimalFormat.format(income) + " 원"
        if(incomeGoal.value == 0){
            _incomePercentage.value = "N"
        }
        else{
            val percentage = (income * 100) / incomeGoal.value
            _incomePercentage.value = if (percentage == 0) "N" else percentage.toString()
        }
    }

    fun getDayLoborWorkBook() {
        viewModelScope.launch {
            workbookRepository.getDetailRecord("일용직근로자", date.value)
                .onSuccess {
                    if (it != null) {
                        val decimalFormat = DecimalFormat("#,###")
                        _workId.value = it.id
                        _workType.value = getDailyWorkType(it.workType)
                        _income.value = decimalFormat.format(it.income)
                        spentAmount.value = decimalFormat.format(it.expenditure)
                        savingsAmount.value = decimalFormat.format(it.saving)
                        dayLaborName.value = it.place
                        dayLaborIncome.value = decimalFormat.format(it.dailyWage)
                        dayLaborType.value = it.typeOfJob
                        _manHour.value = it.numberOfWork
                    }
                }
                .onFailure {
                    UiState.Error(it.message)
                    Timber.d(it.message)
                }
        }
    }

    fun uploadRiderWorkBook(isSave: Boolean) { // 자랑하기 인지 단순 저장인지
        var totalIncome = 0
        totalIncome += dayLaborIncome.value?.replace(",", "")?.toIntOrNull() ?: 0

        viewModelScope.launch {
            val id = workId.value
            val request = if (workType.value == DailyWorkType.REST) {
                RequestDayLaborDiary(
                    workType = workType.value.title,
                    memo = "",
                    income = 0,
                    expenditure = 0,
                    saving = 0,
                    date = date.value,
                    imageUrlList = listOf(),
                    place = "",
                    dailyWage = 0,
                    typeOfJob = "",
                    numberOfWork = 0.0,
                ).toJsonObject()
            } else {
                RequestDayLaborDiary(
                    workType = workType.value.title,
                    memo = "",
                    income = totalIncome,
                    expenditure = spentAmount.value?.replace(",", "")?.toIntOrNull() ?: 0,
                    saving = savingsAmount.value?.replace(",", "")?.toIntOrNull() ?: 0,
                    date = date.value,
                    imageUrlList = listOf(),
                    place = dayLaborName.value ?: "",
                    dailyWage = dayLaborIncome.value?.replace(",", "")?.toIntOrNull() ?: 0,
                    typeOfJob = dayLaborType.value ?: "",
                    numberOfWork = _manHour.value,
                ).toJsonObject()
            }
            if (id != null && id > 0) { // modify
                workbookRepository.modifyCaddieDiary(
                    id,
                    "일용직 근로자",
                    request,
                    null
                ).onSuccess {
                    _workId.value = it.result.id
                    _isUploadedUiState.value = UiState.Success(isSave)
                }.onFailure {
                    Timber.e(it.message)
                }
            } else {
                workbookRepository.uploadCaddieDiary(
                    "일용직 근로자",
                    request,
                    null
                ).onSuccess {
                    _workId.value = it.result.id
                    _isUploadedUiState.value = UiState.Success(isSave)
                }.onFailure {
                    Timber.e(it.message)
                }
            }
        }
    }

    fun getDailyWorkType(name: String): DailyWorkType {
        DailyWorkType.values().onEach { if (it.title == name) return it }
        return DailyWorkType.WORK
    }
}