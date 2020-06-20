package com.example.cashflow.fragments.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cashflow.AppConstants
import com.example.cashflow.AppConstants.EMPTY_STRING
import com.example.cashflow.R
import com.example.cashflow.api.models.Authentication
import com.example.cashflow.databinding.FragmentLoginBinding
import com.example.cashflow.fragments.FragmentBase
import com.example.cashflow.navigation.Navigation
import com.example.cashflow.utils.SharedPreferencesUtil.setAuthenticationToken
import com.example.cashflow.utils.SharedPreferencesUtil.setUserLoggedIn
import com.example.cashflow.views.DialogError.DialogErrorType
import com.google.firebase.iid.FirebaseInstanceId

class FragmentLogin : FragmentBase() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private val onClickListener = View.OnClickListener {
        handleLoginRequest()
    }

    private val onRegisterClickListener = View.OnClickListener {
        navigationHelper.showScreen(Navigation.REGISTER)
    }

    private var firebaseToken: String = EMPTY_STRING
    private lateinit var dataBinding: FragmentLoginBinding

    // views
    private lateinit var spinner: RelativeLayout
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        generateFirebaseToken()
        initializeObservers()
        setUpBinding()
    }

    override fun initializeObservers() {
        viewModel.loginRepository.authentication.observe(viewLifecycleOwner,
            Observer<Authentication.Response> { response ->
                activity?.application?.let {
                    setUserLoggedIn(it, true)
                    setAuthenticationToken(it, (response.token ?: EMPTY_STRING))
                }
                navigationHelper.showScreen(Navigation.HOME)
            })

        viewModel.loginRepository.authenticationError.observe(viewLifecycleOwner,
            Observer<Int> {
                spinner.visibility = View.INVISIBLE
                showErrorDialog(DialogErrorType.AUTHENTICATION)
            })
    }

    override fun setUpBinding() {
        spinner = dataBinding.progressBarContainer
        emailField = dataBinding.emailEditText
        passwordField = dataBinding.passwordEditText
        dataBinding.signInButton.setOnClickListener(onClickListener)
        dataBinding.dontHaveAccountLabel.setOnClickListener(onRegisterClickListener)
    }

    private fun handleLoginRequest() {
        val email = emailField.text?.toString() ?: EMPTY_STRING
        val password = passwordField.text?.toString() ?: EMPTY_STRING
        if (email.isEmpty() || password.isEmpty()) {
            showErrorDialog(DialogErrorType.FORM_NOT_POPULATED)
            return
        }

        if (!AppConstants.EMAIL_MATCHER.toRegex().containsMatchIn(email)) {
            showErrorDialog(DialogErrorType.INVALID_EMAIL)
            return
        }

        val request = Authentication.Request(email, password, firebaseToken)
        viewModel.loginRepository.authenticateUser(request)
        spinner.visibility = View.VISIBLE
    }

    private fun generateFirebaseToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    firebaseToken = it.result?.token ?: EMPTY_STRING
                }
            }
    }
}