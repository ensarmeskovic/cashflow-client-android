package com.example.cashflow.fragments.changePassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cashflow.AppConstants
import com.example.cashflow.AppConstants.EMPTY_STRING
import com.example.cashflow.AppConstants.HTTP_CODE_200
import com.example.cashflow.AppConstants.HTTP_CODE_400
import com.example.cashflow.R
import com.example.cashflow.api.models.ChangePassword
import com.example.cashflow.databinding.FragmentChangePasswordBinding
import com.example.cashflow.fragments.FragmentBase
import com.example.cashflow.views.DialogError

class FragmentChangePassword : FragmentBase() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
    }

    private val onPasswordChangeClickListener = View.OnClickListener {
        handlePasswordChange()
    }

    private lateinit var dataBinding: FragmentChangePasswordBinding

    // view
    private lateinit var spinner: RelativeLayout
    private lateinit var oldPasswordField: EditText
    private lateinit var newPasswordField: EditText
    private lateinit var confirmPassword: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_change_password, container, false
        )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeObservers()
        setUpBinding()
    }

    override fun initializeObservers() {
        viewModel.changePasswordRepository.successfullyChanged.observe(viewLifecycleOwner,
            Observer<Int> {
                spinner.visibility = View.GONE
                when (it) {
                    HTTP_CODE_200 -> {
                        Toast.makeText(
                            activity,
                            "Password successfully changed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        fragmentManager?.popBackStack()
                    }
                    HTTP_CODE_400 -> {
                        showErrorDialog(DialogError.DialogErrorType.INVALID_OLD_PASSWORD)
                    }
                    else -> {
                        showErrorDialog(DialogError.DialogErrorType.SERVER_ERROR)
                    }
                }
            })
    }

    override fun setUpBinding() {
        spinner = dataBinding.progressBarContainer
        oldPasswordField = dataBinding.oldPasswordEditText
        newPasswordField = dataBinding.newPasswordEditText
        confirmPassword = dataBinding.confirmNewPasswordEditText
        dataBinding.changePasswordButton.setOnClickListener(onPasswordChangeClickListener)
    }

    private fun handlePasswordChange() {
        val oldPassword = oldPasswordField.text?.toString() ?: EMPTY_STRING
        val newPassword = newPasswordField.text?.toString() ?: EMPTY_STRING
        val confirmPassword = confirmPassword.text?.toString() ?: EMPTY_STRING

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showErrorDialog(DialogError.DialogErrorType.FORM_NOT_POPULATED)
            return
        }

        if (!AppConstants.PASSWORD_MATCHER.toRegex().containsMatchIn(newPassword)) {
            showErrorDialog(DialogError.DialogErrorType.PASSWORD_TOO_SHORT)
            return
        }

        if (newPassword != confirmPassword) {
            showErrorDialog(DialogError.DialogErrorType.PASSWORDS_NOT_MATCHING)
            return
        }

        val request = ChangePassword.Request(oldPassword, newPassword, confirmPassword)
        viewModel.changePasswordRepository.changePassword(authToken, request)
        spinner.visibility = View.VISIBLE
    }
}