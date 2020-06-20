package com.example.cashflow.views

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.cashflow.AppConstants
import com.example.cashflow.R
import com.example.cashflow.callbacks.InsertDialogCallback

class DialogEdit(
    activity: Activity,
    private val inboxName: String?,
    private val callback: InsertDialogCallback
) : Dialog(activity), View.OnClickListener {

    private lateinit var inboxField: EditText
    private lateinit var positiveButton: Button
    private lateinit var negativeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit_inbox)

        inboxField = findViewById(R.id.inbox_edit_text)
        positiveButton = findViewById(R.id.edit_inbox_button)
        negativeButton = findViewById(R.id.cancel_inbox_button)

        inboxField.setText(inboxName)
        positiveButton.setOnClickListener(this)
        negativeButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val context = v?.context ?: return
        when (v.id) {
            R.id.edit_inbox_button -> {
                val inboxName = inboxField.text?.toString() ?: AppConstants.EMPTY_STRING
                if (inboxName.isEmpty()) {
                    Toast.makeText(context, context.getString(R.string.toast_inbox_name_field_empty), Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                callback.onEditInbox(inboxName)
                dismiss()
            }
            R.id.cancel_inbox_button -> {
                dismiss()
            }
        }
    }
}