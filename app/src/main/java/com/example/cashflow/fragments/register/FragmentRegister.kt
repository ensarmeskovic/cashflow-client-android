package com.example.cashflow.fragments.register

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cashflow.AppConstants
import com.example.cashflow.AppConstants.EMAIL_MATCHER
import com.example.cashflow.AppConstants.HTTP_CODE_200
import com.example.cashflow.AppConstants.HTTP_CODE_400
import com.example.cashflow.AppConstants.HTTP_CODE_500
import com.example.cashflow.AppConstants.PASSWORD_MATCHER
import com.example.cashflow.R
import com.example.cashflow.api.models.Registration
import com.example.cashflow.databinding.FragmentRegisterBinding
import com.example.cashflow.fragments.FragmentBase
import com.example.cashflow.views.DialogError

class FragmentRegister : FragmentBase() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    private val onRegisterClickListener = View.OnClickListener {
        handleRegisterRequest()
    }

    private val onAlreadyHaveAccountClickListener = View.OnClickListener {
        fragmentManager?.popBackStack()
    }

    private lateinit var dataBinding: FragmentRegisterBinding

    // views
    private lateinit var spinner: RelativeLayout
    private lateinit var displayNameField: EditText
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var confirmPasswordField: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_register, container, false
        )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeObservers()
        setUpBinding()
    }

    override fun setUpBinding() {
        spinner = dataBinding.progressBarContainer
        emailField = dataBinding.emailEditText
        passwordField = dataBinding.passwordEditText
        displayNameField = dataBinding.displayNameEditText
        confirmPasswordField = dataBinding.confirmPasswordEditText
        dataBinding.signUpButton.setOnClickListener(onRegisterClickListener)
        dataBinding.alreadyHaveAnAccount.setOnClickListener(onAlreadyHaveAccountClickListener)
    }

    override fun initializeObservers() {
        viewModel.registerRepository.successfullyRegistered.observe(viewLifecycleOwner,
            Observer<Int> {
                spinner.visibility = GONE
                handleResponseCode(it)
            })
    }

    private fun handleResponseCode(httpCode: Int) {
        when (httpCode) {
            HTTP_CODE_400 -> {
                showErrorDialog(DialogError.DialogErrorType.ACCOUNT_ALREADY_EXISTS)
            }

            HTTP_CODE_500 -> {
                showErrorDialog(DialogError.DialogErrorType.SERVER_ERROR)
            }

            HTTP_CODE_200 -> {
                hideKeyboard()
                Toast.makeText(
                    activity,
                    resources.getString(R.string.toast_register_success),
                    Toast.LENGTH_SHORT
                ).show()
                Handler().postDelayed(Runnable { fragmentManager?.popBackStack() }, 1000)
            }
        }
    }

    private fun handleRegisterRequest() {
        val email = emailField.text?.toString() ?: AppConstants.EMPTY_STRING
        val password = passwordField.text?.toString() ?: AppConstants.EMPTY_STRING
        val confirmPassword = confirmPasswordField.text?.toString() ?: AppConstants.EMPTY_STRING
        val displayName = displayNameField.text?.toString() ?: AppConstants.EMPTY_STRING
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || displayName.isEmpty()) {
            showErrorDialog(DialogError.DialogErrorType.FORM_NOT_POPULATED)
            return
        }

        if (!PASSWORD_MATCHER.toRegex().containsMatchIn(password)) {
            showErrorDialog(DialogError.DialogErrorType.PASSWORD_TOO_SHORT)
            return
        }

        if (!EMAIL_MATCHER.toRegex().containsMatchIn(email)) {
            showErrorDialog(DialogError.DialogErrorType.INVALID_EMAIL)
            return
        }

        if (password != confirmPassword) {
            showErrorDialog(DialogError.DialogErrorType.PASSWORDS_NOT_MATCHING)
            return
        }

        val registerRequest = Registration.Request(displayName, email, password, confirmPassword)
        viewModel.registerRepository.registerUser(registerRequest)
        spinner.visibility = VISIBLE
    }
}