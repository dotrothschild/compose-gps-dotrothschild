package com.inzhood.library.gpslibrary.route

import android.location.Location
import com.inzhood.library.gpslibrary.model.TransportSpeeds

class RouteLogic {
    companion object {
        fun addLocation(location: Location) {
            // determine adding to route based on speed and distance
            if (shouldAddLocation(location)) {
                Route.add(location)
            }
        }

        private fun shouldAddLocation(location: Location): Boolean {
            // Check if there are no previous locations
            if (!Route.hasItems()) return true
            // depending on mode, how long min between route point updates. Data in TransportSpeeds.kt
            return when (inferMode(location)) {
                "Walking" -> location.time >= TransportSpeeds.getLocationUpdatesForMode("Walking") // 2 seconds
                "Bicycle" -> location.time >= TransportSpeeds.getLocationUpdatesForMode("Bicycle") //2 sec
                "Scooter" -> location.time >= TransportSpeeds.getLocationUpdatesForMode("Scooter") // 5 sec
                "Automobile" -> location.time >= TransportSpeeds.getLocationUpdatesForMode("Automobile") // 5 sec
                "Helicopter" -> location.time >= TransportSpeeds.getLocationUpdatesForMode("Helicopter") // 5 sec
                else -> false
            }
        }

        private fun inferMode(location: Location): String {
            return when {
                location.speed < TransportSpeeds.getMaxTravelSpeed("Walking") -> "Walking"
                location.speed < TransportSpeeds.getMaxTravelSpeed("Bicycle") -> "Bicycle"
                location.speed < TransportSpeeds.getMaxTravelSpeed("Scooter") -> "Scooter"
                location.speed < TransportSpeeds.getMaxTravelSpeed("Automobile") -> "Automobile"
                else -> "Helicopter"
            }
        }

        fun isValidFileName(fileName: String): Boolean {
            val pattern = "[^\\\\/:*?\"<>|]+"
            val regex = Regex(pattern)
            return fileName.matches(regex)
        }

        fun getVariableNameFromFilename(filename: String): String {
            var varName: String
            try {
                varName = filename.replace(".json", "").replace(" ", "")
                if (!Regex("^[a-zA-Z_][a-zA-Z0-9_]*$").matches(varName)) {
                    varName = "route"
                }
            } catch (e: Exception) {
                varName = "route"
            }
            return varName
        }
    }
}