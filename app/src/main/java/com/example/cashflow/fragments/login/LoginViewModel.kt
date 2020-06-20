package com.example.cashflow.fragments.login

import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    val loginRepository: LoginRepository by lazy {
        LoginRepository()
    }
}