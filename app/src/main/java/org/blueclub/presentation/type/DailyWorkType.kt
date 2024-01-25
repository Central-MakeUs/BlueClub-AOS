package org.blueclub.presentation.type

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import org.blueclub.R

enum class DailyWorkType(
    @StringRes val titleRes: Int,
    @ColorRes val backgroundColor: Int,
    @ColorRes val textColor: Int,
) {
    WORK(
        R.string.workday,
        R.color.primary_background,
        R.color.primary_normal,
    ),
    REST(
        R.string.restday,
        R.color.red_background,
        R.color.red,
    ),
    EARLY(
        R.string.leaveday,
        R.color.coolgray_02,
        R.color.coolgray_06,
    )
}