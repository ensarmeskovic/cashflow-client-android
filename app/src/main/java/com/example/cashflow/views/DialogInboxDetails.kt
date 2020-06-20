package com.example.cashflow.views

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.cashflow.R
import com.example.cashflow.callbacks.InboxDetailsDialogCallback

class DialogInboxDetails(
    activity: Activity,
    private val active: Boolean,
    private val callback: InboxDetailsDialogCallback
) : Dialog(activity), View.OnClickListener {

    private lateinit var editButton: Button
    private lateinit var deactivateButton: Button
    private lateinit var cancelButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_inbox_details)
        editButton = findViewById(R.id.edit_button)
        deactivateButton = findViewById(R.id.deactivate_button)
        cancelButton = findViewById(R.id.cancel_inbox_button)

        if (active) {
            deactivateButton.setText(R.string.deactivate_label)
        } else {
            deactivateButton.setText(R.string.activate_label)
        }
        editButton.setOnClickListener(this)
        deactivateButton.setOnClickListener(this)
        cancelButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val view = v ?: return
        when (view.id) {
            R.id.edit_button -> {
                callback.onEditInboxClicked()
                dismiss()
            }

            R.id.deactivate_button -> {
                callback.onDeactivateInboxClicked()
                dismiss()
            }

            R.id.cancel_inbox_button -> {
                dismiss()
            }
        }
    }
}