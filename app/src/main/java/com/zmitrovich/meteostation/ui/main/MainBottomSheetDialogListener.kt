package com.zmitrovich.meteostation.ui.main

import com.zmitrovich.meteostation.data.model.parameters.MeteoParameters

interface MainBottomSheetDialogListener {

    fun onPropertiesSelected(weatherType: WeatherType, meteoParameters: MeteoParameters)

}