package com.zmitrovich.meteostation.data.remote

import com.zmitrovich.meteostation.data.model.*
import com.zmitrovich.meteostation.ui.main.WeatherType
import javax.inject.Inject


class MockWeatherDataSource @Inject constructor() : WeatherDataSource {

    override suspend fun getMeteorologicalIndicators(weatherType: WeatherType): Result<Map<String, Map<String, Float>>> =
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
                    "2021-01-08" to 24f,
                    "2021-01-09" to 21f,
                    "2021-01-10" to 22f,
                    "2021-01-11" to 27f,
                ),
                "Активная" to mapOf(
                    "2021-01-01" to 21f,
                    "2021-01-02" to 46f,
                    "2021-01-03" to 41f,
                    "2021-01-04" to 37f,
                    "2021-01-05" to 29f,
                    "2021-01-06" to 31f,
                    "2021-01-07" to 27f,
                ),
                "Эффективная" to mapOf(
                    "2021-01-01" to 34f,
                    "2021-01-02" to 45f,
                    "2021-01-03" to 31f,
                    "2021-01-04" to 15f,
                    "2021-01-05" to 43f,
                    "2021-01-06" to 22f,
                    "2021-01-07" to 45f,
                ),
                "Минимальная" to mapOf(
                    "2021-01-01" to 37f,
                    "2021-01-02" to 45f,
                    "2021-01-03" to 33f,
                    "2021-01-04" to 18f,
                    "2021-01-05" to 49f,
                    "2021-01-06" to 21f,
                    "2021-01-07" to 48f,
                ),
                "Максимальная" to mapOf(
                    "2021-01-01" to 30f,
                    "2021-01-02" to 41f,
                    "2021-01-03" to 39f,
                    "2021-01-04" to 11f,
                    "2021-01-05" to 41f,
                    "2021-01-06" to 24f,
                    "2021-01-07" to 40f,
                )
            )
        )
}