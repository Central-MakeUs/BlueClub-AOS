package org.blueclub.presentation.notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.blueclub.data.model.response.ResponseNotice
import org.blueclub.domain.repository.MyPageRepository
import org.blueclub.util.UiState
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository,
) : ViewModel() {
    private val _noticeUiState: MutableStateFlow<UiState<List<ResponseNotice.ResponseNoticeData>>> =
        MutableStateFlow(UiState.Loading)
    val noticeUiState = _noticeUiState.asStateFlow()

    fun getNoticeData() {
        viewModelScope.launch {
            myPageRepository.getNotice()
                .onSuccess {
                    _noticeUiState.value = UiState.Success(it)
                }
                .onFailure {
                    _noticeUiState.value = UiState.Error(it.message)
                }
        }
    }
}