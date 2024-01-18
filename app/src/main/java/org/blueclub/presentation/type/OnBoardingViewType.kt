package org.blueclub.presentation.type

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.blueclub.R

enum class OnBoardingViewType(
    @StringRes val titleRes: Int,
    @StringRes val descriptRes: Int,
    @DrawableRes val imageRes: Int,
) {
    FIRST(
        R.string.onboarding1_title,
        R.string.onboarding1_description,
        R.drawable.img_onboarding1,
    ),
    SECOND(
        R.string.onboarding2_title,
        R.string.onboarding2_description,
        R.drawable.img_onboarding2,
    ),
    THIRD(
        R.string.onboarding3_title,
        R.string.onboarding3_description,
        R.drawable.img_onboarding3,
    ),
}