package com.inzhood.library.gpslibrary.route
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