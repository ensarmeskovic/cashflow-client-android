package com.example.cashflow.api.models

object InboxUser {
    data class Request(val inboxId: Long?, val email: String?)
}