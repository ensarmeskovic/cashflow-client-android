package com.example.cashflow.api.models

import java.io.Serializable

object EditInbox {
    data class Request(val inboxId: Long?, val name: String?) : Serializable
}