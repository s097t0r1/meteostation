package com.zmitrovich.meteostation.data.model

data class Wetness(
    val air: Float,
    val soil: Float
) : WeatherModel
