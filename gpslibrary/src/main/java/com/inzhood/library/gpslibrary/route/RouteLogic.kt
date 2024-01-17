package com.inzhood.library.gpslibrary.route

import android.location.Location
import com.inzhood.library.gpslibrary.model.TransportSpeeds

class RouteLogic {
    companion object {
        fun addLocation(location: Location) {
            Route.add(location) // no logic just do it, ex: first location
        }

        fun shouldAddLocation(oldLocation:Location, newLocation: Location): Boolean {
            // test if meets the minimal distance
            if (Route.hasItems()) {
                val distance = newLocation.distanceTo(oldLocation) // meters
                val minDist = TransportSpeeds.getMinDistForRoutePoint(TransportSpeeds.currentModeKey)
                if (minDist != null && distance > minDist) {
                   return true
                }
            }
            return false
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