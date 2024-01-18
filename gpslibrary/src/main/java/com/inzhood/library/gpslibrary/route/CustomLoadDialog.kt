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
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
            try {
                if (RouteStorage.loadFile(context, filename)) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.file_loaded_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_loading_file),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("FileReadError", "Error reading file", e)
                Toast.makeText(
                    context,
                    context.getString(R.string.error_occurred_loading_file),
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                alertDialog?.dismiss()
            }
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
        fun create(): CustomLoadDialog {
            return CustomLoadDialog()
        }
    }
}
