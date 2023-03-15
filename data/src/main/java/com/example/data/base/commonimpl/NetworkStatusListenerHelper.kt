package com.example.data.base.commonimpl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

interface NetworkStatusListenerHelper {
    fun checkNetworkAvailability (): SharedFlow<Boolean>
    fun getNetworkAvailabilityStatus(): Boolean
    fun init(): MutableStateFlow<Boolean>
}