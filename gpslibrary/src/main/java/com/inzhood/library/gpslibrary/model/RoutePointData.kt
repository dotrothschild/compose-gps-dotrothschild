package com.inzhood.library.gpslibrary.model
/*
 * Copyright (c) Shimon Rothschild, www.dotRothschild.com 2024
 *
 * Please attribute this code if used without significant modifications:
 *  - Include my name, Shimon Rothschild or company name, dotRothschild.com
 *    in the project's credits or documentation.
 *  - Link back to my website or GitHub repository (if applicable).
 *
 * Thank you for respecting my work!
 */

// This is used only to serialize the location data for writing to json
// speed is automatically calculated between two consecutive location updates by the time elapsed between them it is METERS per SECOND
data class RoutePointData(val lat: Double, val lng: Double, val speed: Float, val timeReported: Long)
