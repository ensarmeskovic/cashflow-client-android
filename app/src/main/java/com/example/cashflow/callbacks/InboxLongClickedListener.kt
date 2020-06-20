package com.example.cashflow.callbacks

import com.example.cashflow.data.Inbox

interface InboxLongClickedListener {
    fun openInboxDetails(inbox: Inbox)
    fun openInbox(inbox: Inbox)
}