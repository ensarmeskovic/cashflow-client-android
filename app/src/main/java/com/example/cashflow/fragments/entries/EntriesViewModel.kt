package com.example.cashflow.fragments.entries

import androidx.lifecycle.ViewModel

class EntriesViewModel : ViewModel() {
    val entriesRepository: EntriesRepository by lazy {
        EntriesRepository()
    }
}