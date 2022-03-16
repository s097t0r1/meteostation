package com.zmitrovich.meteostation.data

import com.zmitrovich.meteostation.data.model.*
import com.zmitrovich.meteostation.data.remote.WeatherDataSource
import com.zmitrovich.meteostation.ui.main.WeatherType
import java.util.*
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherDataSource: WeatherDataSource
) : WeatherRepository {

    override suspend fun getMeteorologicalIndicators(weatherType: WeatherType): Result<Map<String, Map<Date, Float>>> {
        return weatherDataSource.getMeteorologicalIndicators(WeatherType.AIR_TEMPERATURE).fold(
            onSuccess = { return@fold Result.success(it.toDomainModel()) },
            onFailure = { return@fold Result.failure(it) }
        )
    }

}