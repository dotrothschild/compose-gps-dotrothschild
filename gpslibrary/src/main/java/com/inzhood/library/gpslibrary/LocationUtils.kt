package com.inzhood.library.gpslibrary

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.inzhood.library.gpslibrary.model.TransportSpeeds.Companion.trueTransportSpeedFlow
import com.inzhood.library.gpslibrary.route.Route
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException



var currentLocationRequest: LocationRequest? = null
var currentLocationCallback: LocationCallback? = null
private fun createLocationRequest(): LocationRequest = LocationRequest.Builder(
    Priority.PRIORITY_HIGH_ACCURACY,
     trueTransportSpeedFlow.value
).setIntervalMillis(trueTransportSpeedFlow.value).build()

fun Location.asString(format: Int = Location.FORMAT_DEGREES): String {
    val latitude = Location.convert(latitude, format)
    val longitude = Location.convert(longitude, format)
    return "Location is: $latitude, $longitude"
}

@SuppressLint("MissingPermission")
suspend fun FusedLocationProviderClient.awaitLastLocation(): Location =
    suspendCancellableCoroutine { continuation ->
        lastLocation.addOnSuccessListener { location ->
            continuation.resume(location)
        }.addOnFailureListener { e ->
            continuation.resumeWithException(e)
        }
    }

@SuppressLint("MissingPermission")
fun FusedLocationProviderClient.locationFlow(): Flow<Location> = callbackFlow {
    val collectionJob = Job()
    val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            for (location in result.locations) {
                val sendResult = trySend(location)
                if (sendResult.isSuccess) {
                    val sentLocation = sendResult.getOrThrow()
                    // Use sentLocation as needed
                    Route.add(location)
                    Log.d("LocationUtils", "Sent location: $sentLocation")
                    } else {
                    // Handle the case where sending failed, potentially due to a closed channel
                    Log.e("LocationUtils", "Sending failed, is channel closed?", sendResult.exceptionOrNull())
                }
            }
        }
    }
    // Request location updates upfront
    requestLocationUpdates(createLocationRequest(), callback, Looper.getMainLooper())
        .addOnFailureListener { e -> close(e) }
    currentLocationCallback = callback

    launch(collectionJob) {
        trueTransportSpeedFlow.collect {
            // Stop the existing location provider and create a new one
            currentLocationRequest?.let {
                currentLocationCallback?.let { removeLocationUpdates(it) }
            }
            // Create a new location request with the updated interval
            val newLocationRequest = createLocationRequest()
            currentLocationRequest = newLocationRequest
            // Start a new location provider with the new interval
            requestLocationUpdates(newLocationRequest, callback, Looper.getMainLooper())
                .addOnFailureListener { e -> close(e) }
            currentLocationCallback = callback

        }
    }
    awaitClose {
        launch {
            collectionJob.cancelAndJoin() // Cancel and wait for completion
            removeLocationUpdates(callback)
        }
    }
}

