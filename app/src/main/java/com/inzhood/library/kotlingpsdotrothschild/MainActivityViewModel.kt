package com.inzhood.library.kotlingpsdotrothschild

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivityViewModel : ViewModel() {

    private val count: MutableState<Int> = mutableIntStateOf(0)
    fun increment() {
        count.value++
        updateLocation()
    }

    private val _currentLocation =
        MutableStateFlow("placeholder location") // THis string is initial value
    val currentLocation: StateFlow<String> = _currentLocation

    private fun updateLocation() {
        _currentLocation.value = count.value.toString()
    }

    // The currently displayed candidate speed, accepted on button click
    private val _candidateSpeed =
        MutableStateFlow("placeholder Speed") // THis string is initial value
    val candidateSpeed: StateFlow<String> = _candidateSpeed
    fun updateCandidateSpeed(newSpeed: String) {
        _candidateSpeed.value = newSpeed
    }

    // The current speed.
    private val _currentSpeed =
        MutableStateFlow("placeholder Speed") // THis string is initial value
    val currentSpeed: StateFlow<String> = _currentSpeed
    fun updateCurrentSpeed() {
        _currentSpeed.value = _candidateSpeed.value
    }


}