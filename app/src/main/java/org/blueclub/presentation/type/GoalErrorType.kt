package org.blueclub.presentation.type

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import org.blueclub.R

enum class GoalErrorType(
    @StringRes val strRes: Int,
    @ColorRes val colorRes: Int,
) {
    CORRECT(
        R.string.goal_setting_correct,
        R.color.primary_normal
    ),
    TOO_LOW(
        R.string.goal_setting_too_low,
        R.color.red
    ),
    TOO_HIGH(
        R.string.goal_setting_too_high,
        R.color.red
    )
}