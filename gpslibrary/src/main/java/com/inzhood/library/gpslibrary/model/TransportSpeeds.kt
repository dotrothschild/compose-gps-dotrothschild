package com.inzhood.library.gpslibrary.model
/*
 * Copyright (c) Shimon Rothschild, www.dotRothschild.com 2024
 *
 * Please attribute this code if used without significant modifications:
 *  - Include my name, Shimon Rothschild or company name, dotRothschild
 *    in the project's credits or documentation.
 *  - Link back to my website or GitHub repository (if applicable).
 *
 * Thank you for respecting my work!
 */
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/*
* This is used to determine how frequently to add a location to the route.
* Update is based on speed.
* */
class TransportSpeeds {
    companion object {

        private val speeds: List<TransportSpeed> = listOf(
            TransportSpeed("Walking", 5.0, 1.39, 2000L), // 2 seconds is 2 1/2 meter
            TransportSpeed("Bicycle", 20.0, 5.56, 2000L), // 2 seconds is 10 meters
            TransportSpeed("Scooter", 40.0, 11.11, 5000L), // 5 seconds is 50 meters
            TransportSpeed("Automobile", 80.0, 22.22, 5000L), // 5 seconds is 1/10 km
            TransportSpeed("Helicopter", 240.0, 66.67, 5000L) // 5 seconds is 1/3 kilometer
        )
        fun getLocationUpdatesForMode(mode: String): Long {
            val transportSpeed = speeds.find { it.modeKey == mode }
            return transportSpeed?.updateFrequencyMs ?: 2000L
        }

        fun getModeKeys(): List<String> {
            return speeds.map { it.modeKey }}

        private var _currentModeKey: String = "Walking"
        val currentModeKey: String = _currentModeKey
        private val _trueTransportSpeedFlow =  MutableStateFlow(getLocationUpdatesForMode("Walking"))
        val trueTransportSpeedFlow: StateFlow<Long> = _trueTransportSpeedFlow
        fun setTransportSpeed(modeKey: String) {
            _currentModeKey = modeKey
             _trueTransportSpeedFlow.value = getLocationUpdatesForMode(modeKey)
        }

        fun getMinDistForRoutePoint(modeKey: String): Double? {
            return speeds.find { it.modeKey == modeKey }
                ?.let { it.speedMps * it.updateFrequencyMs / 1000.0 }
        } // returns null if not found

    }
}

data class TransportSpeed(
    val modeKey: String, // actual string in strings.xml
    val speedKph: Double,
    val speedMps: Double,
    val updateFrequencyMs: Long
)

