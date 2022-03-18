package com.zmitrovich.meteostation.ui.main

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FormField<T> (
    initialValue: T,
    vararg val predicates: (T) -> Boolean
) {

    private val _mutableState = MutableStateFlow(false)
    val state: Flow<Boolean> = _mutableState

    private var afterValidation: (Boolean) -> Unit = {}

    fun setAfterValidationListener(block: (Boolean) -> Unit) {
        afterValidation = block
    }

    var value: T = initialValue
        set(value) {
            field = value
            val isValid = predicates.fold(true) { acc, predicate ->
                acc && predicate(value)
            }
            afterValidation.invoke(isValid)
            _mutableState.value = isValid
        }
}

fun <T> formFieldOf(initialValue: T, vararg predicates: (T) -> Boolean) =
    FormField(initialValue, *predicates)