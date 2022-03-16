package com.zmitrovich.meteostation.data

import com.zmitrovich.meteostation.data.model.*
import java.util.*

interface WeatherRepository {

    suspend fun getAirTemperature(): Result<Map<String, Map<Date, Float>>>

    fun getSoilTemperature(): Result<Map<Date, SoilTemperature>>

    fun getWetness(): Result<Map<Date, Wetness>>

    fun getPressure(): Result<Map<Date, Pressure>>

    fun getSolarRadiation(): Result<Map<Date, SolarRadiation>>

    fun getWindSpeed(): Result<Map<Date, WindSpeed>>
}