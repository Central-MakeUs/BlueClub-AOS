package org.blueclub.presentation.type

import androidx.annotation.StringRes
import org.blueclub.R

enum class JobSettingViewType(
    @StringRes val titleRes: Int,
) {
    GOLF(R.string.job_golf),
    RIDER(R.string.job_rider),
    DELIVERY(R.string.job_delivery),
    INSURANCE(R.string.job_insurance),
    FREELANCER(R.string.job_freelancer),
}