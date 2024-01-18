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
import com.google.gson.Gson
import com.inzhood.library.gpslibrary.model.RoutePointData


class Route private constructor()
{
    companion object {

        private var locations: MutableList<Location> = mutableListOf()
        fun hasItems(): Boolean {
            return locations.size > 0
        }

        internal fun add(location: Location) {
            if (!hasItems()) {
                locations.add(location)
            } else {
                if (RouteLogic.shouldAddLocation(locations.last(),location)) {
                    locations.add(location)
                }
            }
        }

        fun routeToJson() : String {
            return Gson().toJson(locationDataMap()) // Serialize data with Gson
        }
        fun mutableListToRoute(desiredList: MutableList<Location>) {

            locations = desiredList
            /*
            desiredList.let {
                locations.clear()
                locations.addAll(it)
            }+

             */
        }
        private fun locationDataMap(): List<RoutePointData> {
            return  locations.map { //The map function iterates over each Location object in the locations list.
                RoutePointData(
                    it.latitude,
                    it.longitude,
                    it.speed,
                    it.time
                ) // Map Location properties to Data class, json writes by a,b,c
            }
        }
    }

}