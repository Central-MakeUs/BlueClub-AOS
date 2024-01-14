package org.blueclub.presentation.type

import androidx.annotation.StringRes
import org.blueclub.R

enum class AuthSettingPageViewType(
    @StringRes val titleRes: Int,
) {
    JOB(R.string.auth_job_setting),
    YEAR(R.string.auth_year_setting),
    NICKNAME(R.string.auth_nickname_setting),
}