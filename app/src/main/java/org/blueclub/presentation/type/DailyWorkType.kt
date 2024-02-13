package org.blueclub.presentation.type

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import org.blueclub.R

enum class DailyWorkType(
    @StringRes val titleRes: Int,
    @ColorRes val backgroundColor: Int,
    @ColorRes val textColor: Int,
    @ColorRes val detailTextColor: Int,
    val title: String,
) {
    WORK(
        R.string.workday,
        R.color.primary_background,
        R.color.primary_normal,
        R.color.gray_10,
        "근무",
    ),
    REST(
        R.string.restday,
        R.color.red_background,
        R.color.red,
        R.color.gray_10,
        "휴무",
    ),
    EARLY(
        R.string.leaveday,
        R.color.coolgray_02,
        R.color.coolgray_06,
        R.color.black,
        "조퇴",
    ),
    DEFAULT(
        R.string.select,
        R.color.white,
        R.color.gray_04,
        R.color.gray_04,
        "선택",
    );
}