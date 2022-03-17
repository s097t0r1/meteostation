package com.zmitrovich.meteostation.data.model.parameters

import java.util.*

data class MeteoParameters(
    val year: Int,
    val interval: MeteoInterval,
    val from: Date
)

enum class MeteoInterval {
    WEEK,
    MONTH,
    QUARTER,
    YEAR
}