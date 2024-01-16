package com.inzhood.library.gpslibrary

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.inzhood.library.gpslibrary.R

class CustomDialog private constructor(context: Context) {

    private var alertDialog: AlertDialog? = null

    init {
        // Initialization code moved to the show() method
    }

    fun show(context: Context) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.custom_dialog_layout, null)

        // Customize the dialog view if needed
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        titleTextView.text = "Dialog Title"

        val contentTextView: TextView = view.findViewById(R.id.contentTextView)
        contentTextView.text = "Dialog Content"

        val okButton: Button = view.findViewById(R.id.okButton)
        okButton.setOnClickListener {
            // Handle positive button click
            alertDialog?.dismiss()
        }

        val cancelButton: Button = view.findViewById(R.id.cancelButton)
        cancelButton.setOnClickListener {
            // Handle negative button click or dismiss if not needed
            alertDialog?.dismiss()
        }

        builder.setView(view)
        alertDialog = builder.create()
        alertDialog?.show()
    }

    companion object {
        fun create(context: Context): CustomDialog {
            return CustomDialog(context)
        }
    }
}
