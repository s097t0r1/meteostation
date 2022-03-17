package com.zmitrovich.meteostation.data

import com.zmitrovich.meteostation.data.model.*
import com.zmitrovich.meteostation.data.model.parameters.MeteoData
import com.zmitrovich.meteostation.ui.main.WeatherType
import java.util.*

interface WeatherRepository {

    suspend fun getMeteorologicalIndicators(weatherType: WeatherType): Result<MeteoData>

}