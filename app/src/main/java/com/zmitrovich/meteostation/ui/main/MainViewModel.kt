package com.zmitrovich.meteostation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zmitrovich.meteostation.data.WeatherRepository
import com.zmitrovich.meteostation.data.model.parameters.MeteoData
import com.zmitrovich.meteostation.data.model.parameters.MeteoParameters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _meteoData = MutableStateFlow<MeteoData?>(null)
    val meteoData: StateFlow<MeteoData?> = _meteoData

    private val _error = MutableStateFlow(false)
    val error: StateFlow<Boolean> = _error

    fun getWeather(weatherType: WeatherType, meteoParameters: MeteoParameters) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(3000)
            weatherRepository.getMeteorologicalIndicators(WeatherType.AIR_TEMPERATURE)
                .onSuccess {
                    _meteoData.value = it
                    _error.value = false
                }
                .onFailure {
                    _error.value = true
                }
            _isLoading.value = false
        }
    }

    sealed class Event {
        data class WeatherEvent(
            val weatherType: WeatherType,
            val data: Result<Map<String, Map<Date, Float>>>
        ) : Event()

        object Loading : Event()
    }
}