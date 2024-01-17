package com.inzhood.library.gpslibrary.route
/*
Route Class: Its responsibility  is hold route data

 */
import android.location.Location
import com.google.gson.Gson
import com.inzhood.library.gpslibrary.model.RoutePointData
import com.inzhood.library.gpslibrary.model.TransportSpeeds


class Route private constructor()
{
    companion object {

        private var locations: MutableList<Location> = mutableListOf()
        fun hasItems(): Boolean {
            return locations.size > 0
        }

        // SHIMON: CAUTION, THIS SHOULD BE CALLED ONLY FROM RouteLogic!!!!! TODO: Redesign so it works that way
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