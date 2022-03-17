package com.zmitrovich.meteostation.data.model

import java.text.SimpleDateFormat
import java.util.*

class RemoteMeteoData(val value: Map<String, Map<String, Float>>) :
    Map<String, Map<String, Float>> by value

class MeteoData(val value: Map<String, Map<Date, Float>>) : Map<String, Map<Date, Float>> by value


fun RemoteMeteoData.toDomainModel(): MeteoData {
    val simpleDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)

    return MeteoData(
        this.mapValues {
            it.value.mapKeys {
                simpleDateFormatter.parse(it.key) ?: throw RuntimeException("Cannot parse date")
            }
        }
    )
}