package com.zmitrovich.meteostation.data.remote

import com.zmitrovich.meteostation.data.model.*

class MockWeatherDataSource : WeatherDataSource {

    override suspend fun getAirTemperature(): Result<Map<String, Map<String, Float>>> =
        Result.success(
            mapOf(
                "Текущая" to mapOf(
                    "2021-01-01" to 21f,
                    "2021-01-02" to 23f,
                    "2021-01-03" to 22f,
                    "2021-01-04" to 24f,
                    "2021-01-05" to 21f,
                    "2021-01-06" to 22f,
                    "2021-01-07" to 27f,
                ),
                "Активная" to mapOf(
                    "2021-01-01" to 21f,
                    "2021-01-02" to 23f,
                    "2021-01-03" to 22f,
                    "2021-01-04" to 24f,
                    "2021-01-05" to 21f,
                    "2021-01-06" to 22f,
                    "2021-01-07" to 27f,
                )

            )
        )

    override fun getSoilTemperature(): Result<Map<String, SoilTemperature>> {
        TODO("Not yet implemented")
    }

    override fun getWetness(): Result<Map<String, Wetness>> {
        TODO("Not yet implemented")
    }

    override fun getPressure(): Result<Map<String, Pressure>> {
        TODO("Not yet implemented")
    }

    override fun getSolarRadiation(): Result<Map<String, SolarRadiation>> {
        TODO("Not yet implemented")
    }

    override fun getWindSpeed(): Result<Map<String, WindSpeed>> {
        TODO("Not yet implemented")
    }
}