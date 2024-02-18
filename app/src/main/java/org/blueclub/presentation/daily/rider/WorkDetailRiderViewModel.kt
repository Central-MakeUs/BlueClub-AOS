package org.blueclub.presentation.daily.rider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.blueclub.data.model.request.RequestRiderDiary
import org.blueclub.domain.repository.WorkbookRepository
import org.blueclub.presentation.type.DailyWorkType
import org.blueclub.util.UiState
import org.blueclub.util.getDayOfWeek
import timber.log.Timber
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class WorkDetailRiderViewModel @Inject constructor(
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
    private val _deliveryCount = MutableStateFlow(0) // 배달 건수 (필수)
    val deliveryCount = _deliveryCount.asStateFlow()
    val deliveryIncome: MutableStateFlow<String?> = MutableStateFlow(null) // 캐디피(필수)
    private val _promotionCount = MutableStateFlow(0) // 프로모션 건수
    val promotionCount = _promotionCount.asStateFlow()
    val promotionIncome: MutableStateFlow<String?> = MutableStateFlow(null) // 프로모션 수입

    private val _memo = MutableStateFlow("")
    val memo = _memo.asStateFlow()
    private val _income = MutableStateFlow("계산 중이에요")
    val income = _income.asStateFlow()
    val spentAmount: MutableStateFlow<String?> = MutableStateFlow(null)
    val savingsAmount: MutableStateFlow<String?> = MutableStateFlow(null)

    private val _isUploadedUiState: MutableStateFlow<UiState<Boolean>> =
        MutableStateFlow(UiState.Loading)
    val isUploadedUiState = _isUploadedUiState.asStateFlow()

    val isInputCompleted = combine(
        _workType,
        _deliveryCount,
        deliveryIncome
    ) { workType, deliveryCount, deliveryIncome ->
        workType != DailyWorkType.REST && workType != DailyWorkType.DEFAULT
                && deliveryCount > 0
                && (deliveryIncome?.replace(",", "")?.toIntOrNull() ?: 0) > 0
    }.asLiveData()

    val isSaveAvailable = combine(
        _workType,
        _deliveryCount,
        deliveryIncome
    ) { workType, rounding, caddieP ->
        workType == DailyWorkType.REST || (workType != DailyWorkType.DEFAULT
                && rounding > 0
                && (caddieP?.replace(",", "")?.toIntOrNull() ?: 0) > 0)
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

    fun minusDeliveryCount() {
        if (_deliveryCount.value > 0)
            _deliveryCount.value--
    }

    fun plusDeliveryCount() {
        if (_deliveryCount.value < 999)
            _deliveryCount.value++
    }

    fun minusPromotionCount() {
        if (_promotionCount.value > 0)
            _promotionCount.value--
    }

    fun plusPromotionCount() {
        if (_promotionCount.value < 999)
            _promotionCount.value++
    }

    fun calculateIncome() {
        val decimalFormat = DecimalFormat("#,###")
        val income = (deliveryIncome.value?.replace(",", "")?.toIntOrNull() ?: 0) +
                (promotionIncome.value?.replace(",", "")?.toIntOrNull() ?: 0)
        if (income == 0)
            _income.value = "계산 중이에요"
        else
            _income.value = decimalFormat.format(income) + " 원"
    }

    fun getRiderWorkBook() {
        viewModelScope.launch {
            workbookRepository.getDetailRecord("배달라이더", date.value)
                .onSuccess {
                    if (it != null) {
                        val decimalFormat = DecimalFormat("#,###")
                        _workId.value = it.id
                        _workType.value = getDailyWorkType(it.workType)
                        _income.value = decimalFormat.format(it.income)
                        spentAmount.value = decimalFormat.format(it.expenditure)
                        savingsAmount.value = decimalFormat.format(it.saving)
                        _deliveryCount.value = it.numberOfDeliveries
                        deliveryIncome.value = decimalFormat.format(it.incomeOfDeliveries)
                        _promotionCount.value = it.numberOfPromotions
                        promotionIncome.value = decimalFormat.format(it.incomeOfPromotions)
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
        totalIncome += deliveryIncome.value?.replace(",", "")?.toIntOrNull() ?: 0
        totalIncome += promotionIncome.value?.replace(",", "")?.toIntOrNull() ?: 0

        viewModelScope.launch {
            val id = workId.value
            val request = if (workType.value == DailyWorkType.REST) {
                RequestRiderDiary(
                    workType = workType.value.title,
                    memo = "",
                    income = 0,
                    expenditure = 0,
                    saving = 0,
                    date = date.value,
                    imageUrlList = listOf(),
                    numberOfDeliveries = 0,
                    incomeOfDeliveries = 0,
                    numberOfPromotions = 0,
                    incomeOfPromotions = 0,
                ).toJsonObject()
            } else {
                RequestRiderDiary(
                    workType = workType.value.title,
                    memo = "",
                    income = totalIncome,
                    expenditure = spentAmount.value?.replace(",", "")?.toIntOrNull() ?: 0,
                    saving = savingsAmount.value?.replace(",", "")?.toIntOrNull() ?: 0,
                    date = date.value,
                    imageUrlList = listOf(),
                    numberOfDeliveries = deliveryCount.value,
                    incomeOfDeliveries = deliveryIncome.value?.replace(",", "")?.toIntOrNull() ?: 0,
                    numberOfPromotions = promotionCount.value,
                    incomeOfPromotions = promotionIncome.value?.replace(",", "")?.toIntOrNull()
                        ?: 0,
                ).toJsonObject()
            }
            if (id != null && id > 0) { // modify
                workbookRepository.modifyCaddieDiary(
                    id,
                    "배달 라이더",
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
                    "배달 라이더",
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