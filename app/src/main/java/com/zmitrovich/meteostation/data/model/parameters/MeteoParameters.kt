package com.zmitrovich.meteostation.data.model.parameters

import java.util.*

data class MeteoParameters(
    val interval: MeteoInterval,
    val from: Date
)

enum class MeteoInterval(val value: String) {
    WEEK("Неделя"),
    MONTH("Месяц"),
    QUARTER("Квартель"),
    YEAR("Год")
}