package com.inzhood.library.gpslibrary.route

import android.app.Activity
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.inzhood.library.gpslibrary.R
import com.inzhood.library.gpslibrary.route.RouteLogic.Companion.isValidFileName

class RouteUI {
    companion object {
        private var _routeName = ""
        private const val defaultRouteName =
            "route.json" // Shimon: here not RouteStorage, might want to display this a 'default'

        fun showSaveRouteDialog(activity: Activity) {
            if (_routeName.isEmpty()) {
                val buttonSaveText = activity.getString(R.string.save)
                val buttonDiscardText = activity.getString(R.string.discard)
                val textSaveRoute = activity.getString(R.string.saveRoute)
                val textQuestionSaveRoute = activity.getString(R.string.doSaveRoute)

          //      val editText = EditText(activity) // Create an EditText for filename input
                // Inflate your custom layout
                val inflater = LayoutInflater.from(activity)
                val customLayout = inflater.inflate(R.layout.custom_dialog_layout, null)

                val editText = customLayout.findViewById<EditText>(R.id.editText) // Find your EditText in the custom layout

                MaterialAlertDialogBuilder(activity)
                    .setTitle(textSaveRoute)
                    .setMessage(textQuestionSaveRoute)

                    .setView(customLayout) // Add the EditText to the dialog
                    .setPositiveButton(buttonSaveText) { _, _ ->
                        val filename =
                            editText.text.toString().trim()  // Get the filename from the EditText
                        if (isValidFileName(filename)) {
                            _routeName = "$filename.json"
                        }
                        _routeName = filename.ifEmpty {
                            defaultRouteName // Use default filename if empty
                        }
                        RouteStorage.saveToFile(activity, _routeName)
                    }
                    .setNegativeButton(buttonDiscardText) { _, _ ->
                    }
                    .show()
            } else {
                RouteStorage.saveToFile(activity, _routeName)
            }
        }

        fun showGetRouteDialog(activity: Activity) {
            val items = RouteStorage.getFileNamesInDirectory(activity) //arrayOf("file1.txt", "file2.pdf", "file3.docx")  // Replace with your actual file names
            if (items.isEmpty()) {
                // there are no files saved
                Toast.makeText(activity,
                    activity.getString(R.string.there_are_no_saved_routes),Toast.LENGTH_SHORT).show()
                return
            }
            else {
                var filename = items[0]
                val buttonLoadText = activity.getString(R.string.load)
                val buttonCancelText = activity.getString(R.string.cancel)
                val textLoadRoute = activity.getString(R.string.do_load_route)
                val textQuestionLoadRoute = activity.getString(R.string.replaces_current_route)
                MaterialAlertDialogBuilder(activity)
                    .setTitle(textLoadRoute)
                    .setMessage(textQuestionLoadRoute)
                    .setPositiveButton(buttonLoadText) { _, _ ->
                            RouteStorage.openFile(
                                activity,
                                filename
                            ) // Call the method directly on OK
                    }
                    .setNegativeButton(buttonCancelText) { _, _ ->
                    }
                    .apply {
                        if (items.size == 1) {

                            setMessage("File to load: " +items[0])
                            filename = items[0]
                        } else {
                            setItems(items) { _, which ->
                                filename = items[which]
                            }
                        }
                    }
                    .show()
            }
        }
    }
}