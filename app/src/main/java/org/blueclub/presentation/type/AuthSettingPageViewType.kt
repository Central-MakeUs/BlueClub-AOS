package org.blueclub.presentation.type

import androidx.annotation.StringRes
import org.blueclub.R

enum class AuthSettingPageViewType(
    @StringRes val titleRes: Int,
    val progress: Int,
) {
    JOB(R.string.auth_job_setting, 30),
    YEAR(R.string.auth_year_setting, 60),
    NICKNAME(R.string.auth_nickname_setting, 100),
    DEFAULT(R.string.empty, 0);
}