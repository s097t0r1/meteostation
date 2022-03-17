package com.zmitrovich.meteostation.data.remote

import com.zmitrovich.meteostation.data.model.RemoteMeteoData
import com.zmitrovich.meteostation.ui.main.WeatherType
import javax.inject.Inject


class MockWeatherDataSource @Inject constructor() : WeatherDataSource {

    override suspend fun getMeteorologicalIndicators(weatherType: WeatherType): Result<RemoteMeteoData> =
        when (weatherType) {
            WeatherType.AIR_TEMPERATURE -> Result.success(RemoteMeteoData(airTemperature))
            WeatherType.WETNESS -> Result.success(RemoteMeteoData(wetness))
            else -> Result.failure(RuntimeException())
        }
}