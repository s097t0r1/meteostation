package com.zmitrovich.meteostation.data.model.parameters

import androidx.annotation.StringRes
import com.zmitrovich.meteostation.R
import java.util.*

data class MeteoParameters(
    val interval: MeteoInterval,
    val from: Date
)

enum class MeteoInterval(@StringRes val value: Int) {
    WEEK(R.string.interval_week),
    MONTH(R.string.interval_month),
    QUARTER(R.string.interval_quarter),
    YEAR(R.string.interval_year)
}