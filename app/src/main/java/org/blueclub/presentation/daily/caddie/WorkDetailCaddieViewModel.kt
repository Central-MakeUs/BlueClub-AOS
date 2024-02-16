package org.blueclub.presentation.daily.caddie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.blueclub.data.model.request.RequestCaddieDiary
import org.blueclub.domain.repository.WorkbookRepository
import org.blueclub.presentation.type.DailyWorkType
import org.blueclub.util.UiState
import org.blueclub.util.getDayOfWeek
import timber.log.Timber
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class WorkDetailCaddieViewModel @Inject constructor(
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

    private val _workType = MutableStateFlow(DailyWorkType.DEFAULT) // 근무 형태(필수)
    val workType = _workType.asStateFlow()
    private val _rounding = MutableStateFlow(0) // 라운딩 수 (필수)
    val rounding = _rounding.asStateFlow()
    val caddieP: MutableStateFlow<String?> = MutableStateFlow(null) // 캐디피(필수)
    val overP: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _baeto = MutableStateFlow(false)
    val baeto = _baeto.asStateFlow()

    private val _workId: MutableStateFlow<Int?> = MutableStateFlow(null)
    val workId = _workId.asStateFlow()

    private val _isUploadedUiState: MutableStateFlow<UiState<Boolean>> =
        MutableStateFlow(UiState.Loading)
    val isUploadedUiState = _isUploadedUiState.asStateFlow()
    private val _isFetchedUiState: MutableStateFlow<UiState<Boolean>> =
        MutableStateFlow(UiState.Loading)
    val isFetchedUiState = _isFetchedUiState.asStateFlow()

    val isInputCompleted = combine(
        _workType,
        _rounding,
        caddieP
    ) { workType, rounding, caddieP ->
        workType != DailyWorkType.REST && workType != DailyWorkType.DEFAULT && rounding > 0 && (caddieP?.replace(
            ",",
            ""
        )
            ?.toIntOrNull() ?: 0) > 0
    }.asLiveData()

    val isSaveAvailable = combine(
        _workType,
        _rounding,
        caddieP
    ) { workType, rounding, caddieP ->
        workType == DailyWorkType.REST || (workType != DailyWorkType.DEFAULT
                && rounding > 0
                && (caddieP?.replace(",", "")?.toIntOrNull() ?: 0) > 0)
    }.asLiveData()

    private val _memo = MutableStateFlow("")
    val memo = _memo.asStateFlow()
    private val _income = MutableStateFlow("계산 중이에요")
    val income = _income.asStateFlow()
    val spentAmount: MutableStateFlow<String?> = MutableStateFlow(null)
    val savingsAmount: MutableStateFlow<String?> = MutableStateFlow(null)


    fun setDate(date: String) {
        _date.value = date
        _month.value = date.slice(5..6).toIntOrNull() ?: 0
        _day.value = date.slice(8..9).toIntOrNull() ?: 0
        _dow.value = getDayOfWeek(date)
    }

    fun minusRounding() {
        if (_rounding.value > 0)
            _rounding.value--
    }

    fun plusRounding() {
        if (_rounding.value < 999)
            _rounding.value++
    }

    fun setWorkType(workType: DailyWorkType) {
        _workType.value = workType
    }

    fun checkBaeto() {
        _baeto.value = !baeto.value
    }

    fun getCaddieWorkBook() {
        viewModelScope.launch {
            workbookRepository.getDetailRecord("골프캐디", date.value)
                .onSuccess {
                    if (it != null) {
                        val decimalFormat = DecimalFormat("#,###")
                        _workId.value = it.id
                        _workType.value = getDailyWorkType(it.workType)
                        _income.value = decimalFormat.format(it.income)
                        spentAmount.value = decimalFormat.format(it.expenditure)
                        savingsAmount.value = decimalFormat.format(it.saving)
                        _rounding.value = it.rounding
                        caddieP.value = decimalFormat.format(it.caddyFee)
                        overP.value = decimalFormat.format(it.overFee)
                        _baeto.value = it.topDressing
                    }
                }
                .onFailure {
                    UiState.Error(it.message)
                    Timber.d(it.message)
                }
        }
    }

    fun getDailyWorkType(name: String): DailyWorkType {
        DailyWorkType.values().onEach { if (it.title == name) return it }
        return DailyWorkType.WORK
    }

    fun uploadCaddieWorkBook(isSave: Boolean) { // 자랑하기 인지 단순 저장인지
        var totalIncome = 0
        totalIncome += caddieP.value?.replace(",", "")?.toIntOrNull() ?: 0
        totalIncome += overP.value?.replace(",", "")?.toIntOrNull() ?: 0

        viewModelScope.launch {
            val id = workId.value
            val request = if (workType.value == DailyWorkType.REST) {
                RequestCaddieDiary(
                    workType = workType.value.title,
                    memo = "",
                    income = 0,
                    expenditure = 0,
                    saving = 0,
                    date = date.value,
                    imageUrlList = listOf(),
                    rounding = 0,
                    caddyFee = 0,
                    overFee = 0,
                    topDressing = baeto.value,
                ).toJsonObject()
            } else {
                RequestCaddieDiary(
                    workType = workType.value.title,
                    memo = "",
                    income = totalIncome,
                    expenditure = spentAmount.value?.replace(",", "")?.toIntOrNull() ?: 0,
                    saving = savingsAmount.value?.replace(",", "")?.toIntOrNull() ?: 0,
                    date = date.value,
                    imageUrlList = listOf(),
                    rounding = rounding.value,
                    caddyFee = caddieP.value?.replace(",", "")?.toIntOrNull() ?: 0,
                    overFee = overP.value?.replace(",", "")?.toIntOrNull() ?: 0,
                    topDressing = baeto.value,
                ).toJsonObject()
            }
            if (id != null && id > 0) { // modify
                workbookRepository.modifyCaddieDiary(
                    id,
                    "골프 캐디",
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
                    "골프 캐디",
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

    companion object {
        var dateFormat = "yyyy-MM-dd"
    }
}