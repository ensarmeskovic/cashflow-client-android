package com.example.cashflow.api.models

import java.io.Serializable

object Registration {
    data class Request(
        val displayName: String?,
        val email: String?,
        val password: String?,
        val passwordConfirmation: String?
    ) : Serializable
}