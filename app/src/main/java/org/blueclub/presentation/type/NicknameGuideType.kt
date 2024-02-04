package org.blueclub.presentation.type

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import org.blueclub.R

enum class NicknameGuideType(
    @StringRes val strRes: Int,
    @ColorRes val colorRes: Int,
) {
    INVALID_NICKNAME(R.string.wrong_nickname, R.color.red),
    VALID_NICKNAME(R.string.correct_nickname, R.color.primary_normal),
    ALREADY_USED(R.string.already_used_nickname, R.color.red),
    DEFAULT(R.string.empty, R.color.black),
}