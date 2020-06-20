package com.example.cashflow.callbacks

import com.example.cashflow.api.models.AddEntry

interface AddEntryCallback {
    fun onAddPayment(request: AddEntry.PaymentRequest)
    fun onAddExpense(request: AddEntry.ExpenseRequest)
}