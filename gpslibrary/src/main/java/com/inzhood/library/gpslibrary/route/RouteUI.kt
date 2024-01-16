package com.inzhood.library.gpslibrary.route

import android.app.Activity
import com.inzhood.library.gpslibrary.CustomSaveDialog

class RouteUI {
    companion object {
        private var _routeName = ""

        fun showSaveRouteDialog(activity: Activity) {
            if (_routeName.isEmpty()) {
                val customSaveDialog = CustomSaveDialog.create(activity)
                customSaveDialog.show(activity)

            } else {
                RouteStorage.saveToFile(activity, _routeName)
            }
        }
        fun showLoadRouteDialog(activity: Activity) {
            if (_routeName.isEmpty()) {
                val customLoadDialog = CustomLoadDialog.create(activity)
                customLoadDialog.show(activity)
            } else {
                RouteStorage.loadFile(activity, _routeName)
            }
        }
    }
}