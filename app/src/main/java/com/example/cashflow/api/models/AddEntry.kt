package com.example.cashflow.api.models

object AddEntry {
    data class ExpenseRequest(
        val inboxId: Long?,
        val name: String?,
        val amount: Double?,
        val note: String?
    )

    data class PaymentRequest(val inboxId: Long?, val amount: Double?, val note: String?)
}