package com.zmitrovich.meteostation.data

import com.zmitrovich.meteostation.data.model.*
import com.zmitrovich.meteostation.data.remote.WeatherDataSource
import java.util.*

class WeatherRepositoryImpl(
    private val weatherDataSource: WeatherDataSource
) : WeatherRepository {

    override suspend fun getAirTemperature(): Result<Map<String, Map<Date, Float>>> {
        return weatherDataSource.getAirTemperature().fold(
            onSuccess = { return@fold Result.success(it.toDomainModel()) },
            onFailure = { return@fold Result.failure(it) }
        )
    }

    override fun getSoilTemperature(): Result<Map<Date, SoilTemperature>> {
        TODO("Not yet implemented")
    }

    override fun getWetness(): Result<Map<Date, Wetness>> {
        TODO("Not yet implemented")
    }

    override fun getPressure(): Result<Map<Date, Pressure>> {
        TODO("Not yet implemented")
    }

    override fun getSolarRadiation(): Result<Map<Date, SolarRadiation>> {
        TODO("Not yet implemented")
    }

    override fun getWindSpeed(): Result<Map<Date, WindSpeed>> {
        TODO("Not yet implemented")
    }

}