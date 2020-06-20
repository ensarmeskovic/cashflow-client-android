package com.example.cashflow.api.models

import java.io.Serializable

object ChangePassword {
    data class Request(
        val oldPassword: String?,
        val password: String?,
        val passwordConfirmation: String?
    ) : Serializable
}