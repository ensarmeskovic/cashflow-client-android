package com.example.cashflow.callbacks

interface InboxUserManagerDialogCallback {
    fun onAddUser(email: String)
    fun onRemoveUser(email: String)
}