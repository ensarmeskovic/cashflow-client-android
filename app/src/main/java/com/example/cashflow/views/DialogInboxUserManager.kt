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
import com.example.cashflow.callbacks.InboxUserManagerDialogCallback

class DialogInboxUserManager(
    activity: Activity,
    private val callback: InboxUserManagerDialogCallback
) : Dialog(activity), View.OnClickListener {

    private lateinit var inboxField: EditText
    private lateinit var addUserButton: Button
    private lateinit var removeUserButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_user_to_inbox)

        inboxField = findViewById(R.id.inbox_edit_text)
        addUserButton = findViewById(R.id.add_inbox_user_button)
        removeUserButton = findViewById(R.id.remove_inbox_user_button)
        cancelButton = findViewById(R.id.cancel_inbox_button)

        addUserButton.setOnClickListener(this)
        removeUserButton.setOnClickListener(this)
        cancelButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val context = v?.context ?: return
        when (v.id) {
            R.id.add_inbox_user_button -> {
                val email = inboxField.text?.toString() ?: AppConstants.EMPTY_STRING
                if (email.isEmpty()) {
                    Toast.makeText(context, context.getString(R.string.toast_user_email_field_empty), Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                callback.onAddUser(email)
                dismiss()
            }

            R.id.remove_inbox_user_button -> {
                val email = inboxField.text?.toString() ?: AppConstants.EMPTY_STRING
                if (email.isEmpty()) {
                    Toast.makeText(context, context.getString(R.string.toast_user_email_field_empty), Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                callback.onRemoveUser(email)
                dismiss()
            }

            R.id.cancel_inbox_button -> {
                dismiss()
            }
        }
    }
}