package com.example.cashflow.fragments.changePassword

import androidx.lifecycle.ViewModel

class ChangePasswordViewModel : ViewModel() {
    val changePasswordRepository by lazy {
        ChangePasswordRepository()
    }
}