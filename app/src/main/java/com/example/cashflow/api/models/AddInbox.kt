package com.example.cashflow.api.models

import java.io.Serializable

object AddInbox {
    data class Request(val name: String?) : Serializable
}