package com.zmitrovich.meteostation.data.remote

import com.zmitrovich.meteostation.data.model.*

interface WeatherDataSource {

    suspend fun getAirTemperature(): Result<Map<String, Map<String, Float>>>

    fun getSoilTemperature(): Result<Map<String, SoilTemperature>>

    fun getWetness(): Result<Map<String, Wetness>>

    fun getPressure(): Result<Map<String, Pressure>>

    fun getSolarRadiation(): Result<Map<String, SolarRadiation>>

    fun getWindSpeed(): Result<Map<String, WindSpeed>>

}