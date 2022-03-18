package com.zmitrovich.meteostation.ui.main

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FormField<T> private constructor(
    initialValue: T,
    vararg val predicates: (T) -> Boolean
) {

    private val _mutableState = MutableStateFlow(false)
    val state: Flow<Boolean> = _mutableState

    var value: T = initialValue
        set(value) {
            field = value
            val result = predicates.fold(true) { acc, predicate ->
                acc && predicate(value)
            }
            _mutableState.value = result
        }

    companion object {
        fun <T> create(initialValue: T, vararg predicates: (T) -> Boolean) =
            FormField(initialValue, *predicates)
    }
}