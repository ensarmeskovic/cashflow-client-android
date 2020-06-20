package com.example.cashflow.fragments.register

import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    val registerRepository: RegisterRepository by lazy {
        RegisterRepository()
    }
}