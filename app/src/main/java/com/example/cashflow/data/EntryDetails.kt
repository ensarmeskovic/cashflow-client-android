package com.example.cashflow.data

import java.io.Serializable

data class EntryDetails(
    val expenseId: Long?,
    val paymentId: Long?,
    val username: String?,
    val name: String?,
    val currentAmount: Double?,
    val totalAmount: Double?,
    val date: String?,
    val note: String?
) : Serializable