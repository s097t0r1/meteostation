package com.zmitrovich.meteostation.data.model

data class AirTemperature(
    val current: Float,
    val active: Float,
    val effective: Float,
    val min: Float,
    val max: Float,
    val frosts: Float
) : WeatherModel