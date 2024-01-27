package org.blueclub.presentation.model

import androidx.annotation.StringRes
import org.blueclub.presentation.type.DailyWorkDetailOptionType

data class DailyWorkDetailOption(
    val optionType: DailyWorkDetailOptionType,
    @StringRes val titleRes: Int,
    val isNecessary: Boolean,
)
