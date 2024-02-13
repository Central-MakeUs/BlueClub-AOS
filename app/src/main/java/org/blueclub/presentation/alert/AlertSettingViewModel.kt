package org.blueclub.presentation.alert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.blueclub.data.model.request.RequestAgreement
import org.blueclub.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class AlertSettingViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    fun setAgreement(tosAgree: Boolean, pushAgree: Boolean) {
        viewModelScope.launch {
            userRepository.setAgreement(
                RequestAgreement(tosAgree, pushAgree)
            )
        }
    }
}