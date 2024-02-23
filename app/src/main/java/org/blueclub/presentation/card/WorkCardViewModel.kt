package org.blueclub.presentation.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.blueclub.data.model.response.ResponseCard
import org.blueclub.domain.repository.WorkbookRepository
import org.blueclub.util.UiState
import timber.log.Timber
import java.text.DecimalFormat
import javax.inject.Inject


@HiltViewModel
class WorkCardViewModel @Inject constructor(
    private val workbookRepository: WorkbookRepository,
) : ViewModel() {

    private val _workId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val workId = _workId.asStateFlow()
    private val _cardUiState: MutableStateFlow<UiState<ResponseCard.ResponseCardData>> =
        MutableStateFlow(UiState.Loading)
    val cardUiState = _cardUiState.asStateFlow()
    private val _cardData: MutableStateFlow<ResponseCard.ResponseCardData?> = MutableStateFlow(null)
    val cardData = _cardData.asStateFlow()
    private val _income = MutableStateFlow("")
    val income = _income.asStateFlow()
    private val _rank = MutableStateFlow("")
    val rank = _rank.asStateFlow()

    private val _isHighRank = MutableStateFlow(true)
    val isHighRank = _isHighRank.asStateFlow()

    fun setWorkId(workId: Int) {
        _workId.value = workId
    }

    fun setHighRank(isHigh : Boolean){
        _isHighRank.value = isHigh
    }

    fun getCardInfo() {
        if (workId.value == -1) {
            _cardUiState.value = UiState.Error("아이디가 존재하지 않습니다.")
            return
        }

        viewModelScope.launch {
            workbookRepository.getCardDetail(workId.value)
                .onSuccess {
                    _cardUiState.value = UiState.Success(it)
                    _cardData.value = it
                    val decimalFormat = DecimalFormat("#,###")
                    _income.value = decimalFormat.format(it.income)
                    _rank.value = it.rank
                    if(it.rank == "기타")
                        _rank.value = "고생했어요"
                }
                .onFailure {
                    Timber.d(it.message)
                    _cardUiState.value = UiState.Error(it.message)
                }
        }
    }

}