package org.blueclub.presentation.daily

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
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DailyWorkDetailViewModel @Inject constructor(
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

    private val _isUploadedUiState: MutableStateFlow<UiState<Boolean>> =
        MutableStateFlow(UiState.Loading)
    val isUploadedUiState = _isUploadedUiState.asStateFlow()

    val isInputCompleted = combine(
        _workType,
        _rounding,
        caddieP
    ) { workType, rounding, caddieP ->
        workType != DailyWorkType.DEFAULT && rounding > 0 && (caddieP?.replace(",", "")
            ?.toIntOrNull() ?: 0) > 0
    }.asLiveData()

    private val _memo = MutableStateFlow("")
    val memo = _memo.asStateFlow()
    val spentAmount: MutableStateFlow<String?> = MutableStateFlow(null)
    val savingsAmount: MutableStateFlow<String?> = MutableStateFlow(null)


    fun setDate(date: String) {
        _date.value = date
        _month.value = date.slice(5..6).toIntOrNull() ?: 0
        _day.value = date.slice(8..9).toIntOrNull() ?: 0
        _dow.value = getDayOfWeek(date)
    }

    fun getDayOfWeek(date: String): String {
        val cal: Calendar = Calendar.getInstance()
        val df = SimpleDateFormat(dateFormat)
        var day = Date()
        day = df.parse(date) ?: return ""
        cal.time = day
        return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.KOREAN) ?: ""
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

    fun uploadCaddieWorkBook(isSave: Boolean) { // 자랑하기 인지 단순 저장인지
        var totalIncome = 0
        totalIncome += caddieP.value?.replace(",", "")?.toIntOrNull() ?: 0
        totalIncome += overP.value?.replace(",", "")?.toIntOrNull() ?: 0

        viewModelScope.launch {
            workbookRepository.uploadCaddieDiary(
                "골프 캐디",
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
                ).toJsonObject(),
                null
            ).onSuccess {
                _isUploadedUiState.value = UiState.Success(isSave)
            }.onFailure {
                Timber.e(it.message)
            }
        }
    }

    companion object {
        var dateFormat = "yyyy-MM-dd"
    }
}