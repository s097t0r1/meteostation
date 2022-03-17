package com.zmitrovich.meteostation.ui.main

import androidx.annotation.StringRes
import com.zmitrovich.meteostation.R

enum class WeatherType(@StringRes val value: Int) {
    AIR_TEMPERATURE(R.string.meteo_type_air_temperature),
    PRESSURE(R.string.meteo_type_pressure),
    SOIL_TEMPERATURE(R.string.meteo_type_soil_temperature),
    SOLAR_RADIATION(R.string.meteo_type_solar_radiation),
    WETNESS(R.string.meteo_type_wetness),
    WIND_SPEED(R.string.meteo_type_wind_speed)
}