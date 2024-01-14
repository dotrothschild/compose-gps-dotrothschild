package com.inzhood.library.kotlingpsdotrothschild

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivityViewModel : ViewModel() {

    val count: MutableState<Int> = mutableIntStateOf(0)
    fun increment() {
        count.value++
        updateLocation()
    }
    private val _currentLocation =  MutableStateFlow("placeholder location") // THis string is initial value
    val currentLocation: StateFlow<String> = _currentLocation

    private fun updateLocation() {
        _currentLocation.value = "new location: " + count.value.toString()
    }
}