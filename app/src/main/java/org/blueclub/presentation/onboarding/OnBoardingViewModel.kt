package org.blueclub.presentation.onboarding

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.blueclub.presentation.type.OnBoardingViewType

class OnBoardingViewModel : ViewModel() {
    private val _onBoardingType = MutableStateFlow(OnBoardingViewType.FIRST)
    val onboardingType = _onBoardingType.asStateFlow()

    fun setOnboardingType(position: Int) {
        _onBoardingType.value = when (position) {
            OnBoardingViewType.FIRST.ordinal -> OnBoardingViewType.FIRST
            OnBoardingViewType.SECOND.ordinal -> OnBoardingViewType.SECOND
            else -> OnBoardingViewType.THIRD
        }
    }
}