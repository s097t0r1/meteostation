package com.zmitrovich.meteostation.data.model

import java.text.SimpleDateFormat
import java.util.*

fun  Map<String, Map<String, Float>>.toDomainModel(): Map<String, Map<Date, Float>> {
    val simpleDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
    return this.mapValues {
        it.value.mapKeys { simpleDateFormatter.parse(it.key) ?: throw RuntimeException("Cannot parse date") }
    }
}

fun <K, V> Map<K,V>.getFirstEntry(): Map.Entry<K, V> =
    this.iterator().next()