package com.zmitrovich.meteostation.data

import com.zmitrovich.meteostation.data.model.MeteoData
import com.zmitrovich.meteostation.data.model.toDomainModel
import com.zmitrovich.meteostation.data.remote.WeatherDataSource
import com.zmitrovich.meteostation.ui.main.WeatherType
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherDataSource: WeatherDataSource
) : WeatherRepository {

    override suspend fun getMeteorologicalIndicators(weatherType: WeatherType): Result<MeteoData> {
        return weatherDataSource.getMeteorologicalIndicators(weatherType).fold(
            onSuccess = { return@fold Result.success(it.toDomainModel()) },
            onFailure = { return@fold Result.failure(it) }
        )
    }

}