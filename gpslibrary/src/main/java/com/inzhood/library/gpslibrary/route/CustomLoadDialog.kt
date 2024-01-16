package com.inzhood.library.gpslibrary.route

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import com.inzhood.library.gpslibrary.R
import com.inzhood.library.gpslibrary.isValidFileName


class CustomLoadDialog private constructor() {

    private var alertDialog: AlertDialog? = null

    @SuppressLint("ClickableViewAccessibility")
    fun show(context: Context) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.custom_dialog_layout, null)
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        titleTextView.text = context.getString(R.string.load_route)

        val contentTextView: TextView = view.findViewById(R.id.contentTextView)
        contentTextView.text = context.getString(R.string.do_load_route)
        val cancelButton: Button = view.findViewById(R.id.cancelButton)
        cancelButton.setOnClickListener {
            // Handle negative button click or dismiss if not needed
            alertDialog?.dismiss()
        }

        val okButton: Button = view.findViewById(R.id.okButton)
        okButton.setOnClickListener {
            val fileView: TextView = view.findViewById(R.id.fileName)
            val filename = fileView.text.toString().trim()
            if (filename.isValidFileName()) {
                RouteStorage.loadFile(context, filename)
                alertDialog?.dismiss()
            } else {
                // Provide clear visual feedback to the user:
                fileView.error =  context.getString(R.string.invalid_filename)
                fileView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFF5F5F5"))  // Light red tint
            }
        }
        // Set an OnTouchListener for the fileView
        val fileView: TextView = view.findViewById(R.id.fileName)
        fileView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Clear the error message and reset the background tint on touch
                fileView.error = null
                fileView.backgroundTintList = null
            }
            false
        }
        builder.setView(view)
        alertDialog = builder.create()
        alertDialog?.show()
    }


    companion object {
        fun create(context: Context): CustomLoadDialog {
            return CustomLoadDialog()
        }
    }
}
