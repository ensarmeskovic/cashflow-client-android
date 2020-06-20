package com.example.cashflow.fragments.menu

import androidx.lifecycle.ViewModel

class MenuViewModel : ViewModel() {
    val menuRepository: MenuRepository by lazy {
        MenuRepository()
    }
}