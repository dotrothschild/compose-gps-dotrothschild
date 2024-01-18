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
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.gson.Gson
import com.inzhood.library.gpslibrary.model.RoutePointData
import java.io.File

class RouteStorage {
    companion object {
        private const val subDirectoryName = "routes"

        fun getFileNamesInDirectory(context: Context): Array<String> {
            val directory = File(context.filesDir, subDirectoryName)

            // Ensure the directory exists
            if (!directory.exists()) {
                return emptyArray()
            }

            // Check if it's a directory
            if (!directory.isDirectory) {
                return emptyArray()
            }

            // Get list of files, ignoring subdirectories
            val files = directory.listFiles { file -> file.isFile }

            // Convert list of filenames to an array
            return files?.map { it.name }?.toTypedArray() ?: emptyArray()
        }

        fun saveToFile(context: Context, filename: String): Boolean {
            var success = false
            try {
                val filesDir = context.filesDir
                val routesDir = File(filesDir, subDirectoryName)

                // Ensure the routes directory exists
                if (!routesDir.exists()) {
                    routesDir.mkdirs()  // Create the directory and any necessary parent directories
                }

                val file = File(routesDir, filename) //create file
                file.writeText(Route.routeToJson())
                success = true
            } catch (e: Exception) {
                Log.e("***DotR***", "Failed to write file " + filename + "Error: " + e.message)
            }
            return success
        }

        fun loadFile(context: Context, filename: String): Boolean {
            var success = false
            try {
                val result = loadFromFile(context, filename)
                if (result != null) {
                    Route.mutableListToRoute(result)
                }
                success = true
            } catch (e: Exception) {
                Log.e("***DotR***", "Failed to read file " + filename + "Error: " + e.message)
            }
            return success
        }

        private fun loadFromFile(context: Context, filename: String): MutableList<Location>? {
            val locationsData = loadMapFromJson(context, filename)
            return if (locationsData.isNullOrEmpty()) {
                // FUTURE: File not loaded, figure out why not
                null
            } else {
                // The list is data type locationData data class LocationData(val lat: Double, val lng: Double, val speed: Float, val timeReported: Long)
                val key = "route"
                locationsData[key]
            }
        }

        private fun loadMapFromJson(
            context: Context,
            filename: String
        ): Map<String, MutableList<Location>>? {
            try {
                val routesDir =
                    File(context.filesDir, subDirectoryName)  // Reference the routes subdirectory
                val file = File(
                    routesDir,
                    filename
                )            // Construct the file path within the subdirectory
                val jsonString = file.readText()

                // Deserialize JSON into LocationData objects
                val locationDataList =
                    Gson().fromJson(jsonString, Array<RoutePointData>::class.java).toList()

                // Create Location objects from LocationData and store in a map
                val routeMap = mutableMapOf<String, MutableList<Location>>()
                val routeName =
                    RouteLogic.getVariableNameFromFilename(filename) // Extract valid variable name
                routeMap[routeName] = locationDataList.map {
                    Location("").apply {
                        latitude = it.lat
                        longitude = it.lng
                        speed = it.speed
                        time = it.timeReported
                    }
                }.toMutableList()

                return routeMap // Return the map containing the named route list
            } catch (e: Exception) {
                Log.e("core.library.model", "Unable to load route", e)
                return null // Indicate failure by returning null
            }
        }
    }
}