package com.example.sonder_dating_app.presentation.utils.extensions

import androidx.lifecycle.MutableLiveData

fun <T: Any?> MutableLiveData<T>.default(initialValue: T) = apply { value = initialValue }

fun <T: Any?> MutableLiveData<T>.set(value: T) = apply { this.value = value }