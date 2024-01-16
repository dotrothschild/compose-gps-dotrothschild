package com.inzhood.library.kotlingpsdotrothschild

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.inzhood.library.gpslibrary.awaitLastLocation
import com.inzhood.library.gpslibrary.locationFlow
import com.inzhood.library.gpslibrary.route.RouteLogic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch

class MainActivityViewModel(context: Context) : ViewModel() {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> = _location
    fun getLastKnownLocation() {
        viewModelScope.launch {
            try {
                val lastLocation = fusedLocationClient.awaitLastLocation()
                _location.value = lastLocation // Update LiveData with last known location
            } catch (e: Exception) {
                Log.e("GpsLocationViewModel", "Unable to get last known location", e)
                // TODO:  Consider notifying the UI about the error
            }
        }
    }
    fun startUpdatingLocation() {
        viewModelScope.launch { // Use viewModelScope for lifecycle-aware coroutines
            fusedLocationClient.locationFlow()
                .conflate()
                .catch { e ->
                    Log.e("GpsLocationViewModel", "Unable to get location", e)
                    // TODO: notifying the UI about the error
                }
                .collect { location ->
                    RouteLogic.addLocation(location)
                    _location.value = location
                }
        }
    }



    //  ******************  new code ****************
    private val count: MutableState<Int> = mutableIntStateOf(0)
    fun increment() {
        count.value++
        updateLocation()
    }

    private val _currentLocation =
        MutableStateFlow("placeholder location") // THis string is initial value
    val currentLocation: StateFlow<String> =  _currentLocation

    private fun updateLocation() {
        _currentLocation.value = _location.value.toString() // for testing only count.value.toString()
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

// *************************************   FACTORY  ********************************************
class MainActivityViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainActivityViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}