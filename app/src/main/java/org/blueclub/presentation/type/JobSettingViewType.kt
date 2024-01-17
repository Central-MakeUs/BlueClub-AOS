package org.blueclub.presentation.type

import androidx.annotation.StringRes
import org.blueclub.R

enum class JobSettingViewType(
    @StringRes val titleRes: Int,
) {
    GOLF(R.string.job_golf),
    RIDER(R.string.job_rider),
    DAY_LABOR(R.string.job_day_labor),
}