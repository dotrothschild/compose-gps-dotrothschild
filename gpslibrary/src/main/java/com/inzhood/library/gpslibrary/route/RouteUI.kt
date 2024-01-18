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
import android.app.Activity

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
                val customLoadDialog = CustomLoadDialog.create()
                customLoadDialog.show(activity)
            } else {
                RouteStorage.loadFile(activity, _routeName)
            }
        }
    }
}