package com.example.cashflow.api.models

import java.io.Serializable

object Authentication {
    data class Response(val token: String?, val tokenExpiresInMinutes: Long?) :
        Serializable

    data class Request(
        val email: String?,
        val password: String?,
        val deviceToken: String?
    ) : Serializable
}