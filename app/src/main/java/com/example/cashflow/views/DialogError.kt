package com.example.cashflow.views

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.cashflow.R

class DialogError(
    activity: Activity,
    private val errorType: DialogErrorType
) : Dialog(activity), View.OnClickListener {

    private lateinit var closeButton: ImageView
    private lateinit var dialogImage: ImageView
    private lateinit var dialogErrorTitle: TextView
    private lateinit var dialogErrorDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_error_authenticating)
        closeButton = findViewById(R.id.close_button)
        dialogImage = findViewById(R.id.dialog_image)
        dialogErrorTitle = findViewById(R.id.dialog_header)
        dialogErrorDescription = findViewById(R.id.dialog_description)
        initializeDialogDetails()
    }

    override fun onClick(v: View?) {
        dismiss()
    }

    private fun initializeDialogDetails() {
        when (errorType) {
            DialogErrorType.AUTHENTICATION -> {
                dialogImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_authentication_error
                    )
                )
                dialogErrorTitle.text = context.resources.getString(R.string.auth_error_header)
                dialogErrorDescription.text =
                    context.resources.getString(R.string.auth_error_description)
            }
            DialogErrorType.PASSWORDS_NOT_MATCHING -> {
                dialogImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_passwords_not_matching_error
                    )
                )
                dialogErrorTitle.text =
                    context.resources.getString(R.string.passwords_not_matching_error_header)
                dialogErrorDescription.text =
                    context.resources.getString(R.string.passwords_not_matching_error_description)
            }
            DialogErrorType.PASSWORD_TOO_SHORT -> {
                dialogImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_password_too_short_error
                    )
                )
                dialogErrorTitle.text =
                    context.resources.getString(R.string.password_too_short_error_header)
                dialogErrorDescription.text =
                    context.resources.getString(R.string.password_too_short_error_description)
            }
            DialogErrorType.ACCOUNT_ALREADY_EXISTS -> {
                dialogImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_authentication_error
                    )
                )
                dialogErrorTitle.text =
                    context.resources.getString(R.string.already_exists_error_header)
                dialogErrorDescription.text =
                    context.resources.getString(R.string.already_exists_error_description)
            }
            DialogErrorType.SERVER_ERROR -> {
                dialogImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_authentication_error
                    )
                )
                dialogErrorTitle.text = context.resources.getString(R.string.server_error_header)
                dialogErrorDescription.text =
                    context.resources.getString(R.string.server_error_description)
            }
            DialogErrorType.FORM_NOT_POPULATED -> {
                dialogImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_authentication_error
                    )
                )
                dialogErrorTitle.text =
                    context.resources.getString(R.string.form_not_populated_header)
                dialogErrorDescription.text =
                    context.resources.getString(R.string.form_not_populated_description)
            }
            DialogErrorType.INVALID_EMAIL -> {
                dialogImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_invalid_email
                    )
                )
                dialogErrorTitle.text = context.resources.getString(R.string.invalid_email_header)
                dialogErrorDescription.text =
                    context.resources.getString(R.string.invalid_email_description)
            }
            DialogErrorType.INVALID_OLD_PASSWORD -> {
                dialogImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_passwords_not_matching_error
                    )
                )
                dialogErrorTitle.text =
                    context.resources.getString(R.string.invalid_old_password_header)
                dialogErrorDescription.text =
                    context.resources.getString(R.string.invalid_old_password_description)
            }
        }
        closeButton.setOnClickListener(this)
    }

    enum class DialogErrorType {
        AUTHENTICATION,
        ACCOUNT_ALREADY_EXISTS,
        SERVER_ERROR,
        FORM_NOT_POPULATED,
        PASSWORDS_NOT_MATCHING,
        PASSWORD_TOO_SHORT,
        INVALID_EMAIL,
        INVALID_OLD_PASSWORD
    }
}