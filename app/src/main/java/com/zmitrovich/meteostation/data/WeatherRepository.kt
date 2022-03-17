package com.zmitrovich.meteostation.data

import com.zmitrovich.meteostation.data.model.MeteoData
import com.zmitrovich.meteostation.ui.main.WeatherType

interface WeatherRepository {

    suspend fun getMeteorologicalIndicators(weatherType: WeatherType): Result<MeteoData>

}