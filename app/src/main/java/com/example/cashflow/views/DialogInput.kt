package com.example.cashflow.views

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.cashflow.AppConstants
import com.example.cashflow.R
import com.example.cashflow.callbacks.InsertDialogCallback

class DialogInput(
    activity: Activity,
    private val callback: InsertDialogCallback
) : Dialog(activity), View.OnClickListener {

    private lateinit var inboxHeader: TextView
    private lateinit var inboxField: EditText
    private lateinit var positiveButton: Button
    private lateinit var negativeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_inbox)

        inboxHeader = findViewById(R.id.add_inbox_header)
        inboxField = findViewById(R.id.inbox_edit_text)
        positiveButton = findViewById(R.id.insert_inbox_button)
        negativeButton = findViewById(R.id.cancel_inbox_button)

        positiveButton.setOnClickListener(this)
        negativeButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val context = v?.context ?: return
        when (v.id) {
            R.id.insert_inbox_button -> {
                val inboxName = inboxField.text?.toString() ?: AppConstants.EMPTY_STRING
                if (inboxName.isEmpty()) {
                    Toast.makeText(context, context.getString(R.string.toast_inbox_name_field_empty), Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                callback.onInsertInbox(inboxName)
                dismiss()
            }
            R.id.cancel_inbox_button -> {
                dismiss()
            }
        }
    }
}