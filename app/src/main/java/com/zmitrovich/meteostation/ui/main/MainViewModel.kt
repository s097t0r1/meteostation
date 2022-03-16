package com.zmitrovich.meteostation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zmitrovich.meteostation.data.WeatherRepository
import com.zmitrovich.meteostation.data.model.WeatherModel
import com.zmitrovich.meteostation.data.model.parameters.WeatherParameters
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun getWeather(weatherType: WeatherType, weatherParameters: WeatherParameters) {
        viewModelScope.launch {
            eventChannel.send(Event.Loading)
            val weatherResult = when (weatherType) {
                WeatherType.AIR_TEMPERATURE -> weatherRepository.getAirTemperature()
                else -> throw RuntimeException()
            }
            eventChannel.send(Event.WeatherEvent(weatherType, weatherResult))
        }
    }

    sealed class Event {
        data class WeatherEvent(val weatherType: WeatherType, val data: Result<Map<String, Map<Date, Float>>>) : Event()
        object Loading : Event()
    }
}