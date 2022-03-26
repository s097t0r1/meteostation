package com.zmitrovich.meteostation.ui.main_bottom_sheet

import com.zmitrovich.meteostation.data.model.parameters.MeteoParameters
import com.zmitrovich.meteostation.ui.main.WeatherType

interface MainBottomSheetDialogListener {

    fun onPropertiesSelected(weatherType: WeatherType, meteoParameters: MeteoParameters)

}