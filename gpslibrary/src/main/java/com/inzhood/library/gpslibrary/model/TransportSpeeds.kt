package com.inzhood.library.gpslibrary.model
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
            TransportSpeed("Helicopter", 240.0, 66.67, 5000) // 5 seconds is 1/3 kilometer
        )
        fun getLocationUpdatesForMode(mode: String): Long {
            val transportSpeed = speeds.find { it.modeKey == mode }
            return transportSpeed?.updateFrequencyMs ?: 2000L
        }
        // for updating the route array get the max speed of a particular type of transport
        fun getMaxTravelSpeed(mode: String): Double {
            val transportSpeed = speeds.find { it.modeKey == mode }
            return transportSpeed?.speedMps ?: speeds[0].speedMps
        }
        fun getModeKeys(): List<String> {
            return speeds.map { it.modeKey }}
    }
}

data class TransportSpeed(
    val modeKey: String, // actual string in strings.xml
    val speedKph: Double,
    val speedMps: Double,
    val updateFrequencyMs: Long
)

