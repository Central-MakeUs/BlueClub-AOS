package org.blueclub.presentation.type

import androidx.annotation.StringRes
import org.blueclub.R

enum class JobSettingViewType(
    @StringRes val titleRes: Int,
    val title: String,
) {
    GOLF(R.string.job_golf, "골프캐디"),
    RIDER(R.string.job_rider, "배달라이더"),
    DAY_LABOR(R.string.job_day_labor, "일용직 노동자"),
}