package com.zmitrovich.meteostation.ui.main

import androidx.lifecycle.ViewModel
import com.zmitrovich.meteostation.data.model.parameters.MeteoInterval
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MainBottomSheetViewModel @Inject constructor() : ViewModel() {

    val startDateField = formFieldOf(
        "",
        { it.isNotEmpty() },
        { it.length > 2 }
    )

    val samplePeriodField = formFieldOf<MeteoInterval?>(
        null,
        { it != null }
    )

    val weatherTypeField = formFieldOf<WeatherType?>(
        null,
        { it != null }
    )

   val formIsValid = combine(startDateField.state, samplePeriodField.state, weatherTypeField.state) { arr ->
        arr.fold(true) { acc, value -> value && acc}
    }

}