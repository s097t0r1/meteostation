package com.zmitrovich.meteostation.data.remote

import com.zmitrovich.meteostation.data.model.*
import com.zmitrovich.meteostation.data.model.parameters.RemoteMeteoData
import com.zmitrovich.meteostation.ui.main.WeatherType

interface WeatherDataSource {

    suspend fun getMeteorologicalIndicators(weatherType: WeatherType): Result<RemoteMeteoData>

}