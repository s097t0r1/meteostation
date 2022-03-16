package com.zmitrovich.meteostation.data.model

data class SoilTemperature(
    val land: Float,
    val depth: Float
) : WeatherModel
