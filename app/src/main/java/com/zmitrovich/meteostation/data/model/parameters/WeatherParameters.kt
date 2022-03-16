package com.zmitrovich.meteostation.data.model.parameters

import java.util.*

data class WeatherParameters(
    val year: Int,
    val interval: WeatherInterval,
    val from: Date
)

enum class WeatherInterval {
    WEEK,
    MONTH,
    QUARTER,
    YEAR
}