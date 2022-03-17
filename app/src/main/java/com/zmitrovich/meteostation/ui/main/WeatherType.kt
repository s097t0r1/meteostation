package com.zmitrovich.meteostation.ui.main

enum class WeatherType(val value: String) {
    AIR_TEMPERATURE("Температура воздуха"),
    PRESSURE("Давление"),
    SOIL_TEMPERATURE("Температура почвы"),
    SOLAR_RADIATION("Солнечная радиация"),
    WETNESS("Влажность"),
    WIND_SPEED("Скорость ветра")
}