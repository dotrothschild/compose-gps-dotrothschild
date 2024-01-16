package com.inzhood.library.gpslibrary

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat
// This code uses LocationUtils.kt for .asString
fun Activity.findAndSetText(@IdRes id: Int, text: String) {
    findViewById<TextView>(id).text = text
}

fun Activity.showLocation(@IdRes id: Int, location: Location?) {
    if (location != null) {
        findAndSetText(id, location.asString(Location.FORMAT_MINUTES))
    } else {
        findAndSetText(id, "Location unknown")
    }
}

fun Activity.hasPermission(permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun String.isValidFileName(): Boolean {
    // more restrictive. only letters, numbers, underscores, and hyphens,
    val pattern = "[\\w-]+" // "[^\\\\/:*?\"<>|]+"
    val regex = Regex(pattern)
    return regex.matches(this)
}
