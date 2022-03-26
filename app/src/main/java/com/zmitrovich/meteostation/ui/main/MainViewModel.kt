package com.zmitrovich.meteostation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zmitrovich.meteostation.data.WeatherRepository
import com.zmitrovich.meteostation.data.model.MeteoData
import com.zmitrovich.meteostation.data.model.parameters.MeteoParameters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<MainViewState>(MainViewState.Initial)
    val viewState: StateFlow<MainViewState> = _viewState

    fun getWeather(weatherType: WeatherType, meteoParameters: MeteoParameters) {
        viewModelScope.launch {
            _viewState.value = MainViewState.Loading
            delay(3000)
            weatherRepository.getMeteorologicalIndicators(weatherType)
                .onSuccess {
                    _viewState.value = MainViewState.DisplayMeteoData(
                        weatherType,
                        meteoParameters,
                        it
                    )
                }
                .onFailure {
                    _viewState.value = MainViewState.Error(it)
                }
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