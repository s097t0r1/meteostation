package com.zmitrovich.meteostation.data.model

import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

fun <K, V> Map<K,V>.getFirstEntry(): Map.Entry<K, V> =
    this.iterator().next()

var LineData.lineColors: List<Int>
    get() {
        return colors.toList()
    }
    set(value) {
        for ((index, dataSet) in dataSets.withIndex()) {
            (dataSet as? LineDataSet)?.color = value[index % value.size]
        }
    }