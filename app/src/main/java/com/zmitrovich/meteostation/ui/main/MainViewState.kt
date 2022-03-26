package com.zmitrovich.meteostation.ui.main

import com.zmitrovich.meteostation.data.model.MeteoData
import com.zmitrovich.meteostation.data.model.parameters.MeteoParameters

sealed class MainViewState {
    object Initial : MainViewState()
    class DisplayMeteoData(
        val weatherType: WeatherType,
        val meteoParameters: MeteoParameters,
        val data: MeteoData
    ) : MainViewState()
    object Loading : MainViewState()
    class Error(val throwable: Throwable) : MainViewState()
}
