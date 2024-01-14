package org.blueclub.presentation.type

import androidx.annotation.StringRes
import org.blueclub.R

enum class AuthSettingPageViewType(
    @StringRes val titleRes: Int,
    val progress: Int,
) {
    JOB(R.string.auth_job_setting, 25),
    YEAR(R.string.auth_year_setting, 50),
    NICKNAME(R.string.auth_nickname_setting, 75),
    DEFAULT(R.string.empty, 0);
}