package com.example.cashflow.fragments.home

import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    val homeRepository: HomeRepository by lazy {
        HomeRepository()
    }
}