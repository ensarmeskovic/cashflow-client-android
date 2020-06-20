package com.example.cashflow.data

import java.io.Serializable

data class Entry(
    val expenses: Double?,
    val payments: Double?,
    val balance: Double?,
    val entries: List<EntryDetails>?
) : Serializable