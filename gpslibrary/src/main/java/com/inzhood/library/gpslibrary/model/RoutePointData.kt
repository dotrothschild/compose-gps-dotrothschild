package com.inzhood.library.gpslibrary.model
// This is used only to serialize the location data for writing to json
// speed is automatically calculated between two consecutive location updates by the time elapsed between them it is METERS per SECOND
data class RoutePointData(val lat: Double, val lng: Double, val speed: Float, val timeReported: Long)
