package com.example.cashflow.fragments.home

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cashflow.AppConstants.EMPTY_STRING
import com.example.cashflow.R
import com.example.cashflow.callbacks.InboxLongClickedListener
import com.example.cashflow.data.Inbox
import com.example.cashflow.utils.DateTimeUtils.getFormattedInboxDate
import java.text.SimpleDateFormat
import java.util.*


class InboxAdapter(
    private val callback: InboxLongClickedListener
) : RecyclerView.Adapter<InboxAdapter.InboxViewHolder>() {

    private val inboxList: ArrayList<Inbox> = arrayListOf()

    fun setInboxList(inboxList: List<Inbox>) {
        this.inboxList.clear()
        this.inboxList.addAll(inboxList)
        this.inboxList.sortBy { it.inboxId }
        notifyDataSetChanged()
    }

    fun getInboxList(): ArrayList<Inbox> {
        return inboxList
    }

    fun getInboxIdByAdapterPosition(position: Int): Long {
        return inboxList[position].inboxId ?: -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InboxViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.inbox_item, parent, false)
        return InboxViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return inboxList.size
    }

    override fun onBindViewHolder(holder: InboxViewHolder, position: Int) {
        val context = holder.itemView.context ?: return
        val inbox = inboxList[position]
        holder.itemView.setOnLongClickListener {
            callback.openInboxDetails(inbox)
            true
        }

        holder.itemView.setOnClickListener {
            callback.openInbox(inbox)
        }

        val date = inbox.date?.take(10) ?: EMPTY_STRING
        if (date.isNotEmpty()) {
            val dateFormatted: Date? = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)
            if (dateFormatted != null) {
                holder.inboxDate.text = String.format(
                    context.getString(
                        R.string.inbox_last_updated,
                        getFormattedInboxDate(context, dateFormatted)
                    )
                )
            } else {
                holder.inboxDate.text = EMPTY_STRING
            }
        } else {
            holder.inboxDate.text = EMPTY_STRING
        }

        holder.inboxName.text = inbox.name ?: EMPTY_STRING
        val seenAll = inbox.seenAll ?: false
        if (seenAll) {
            holder.notification_icon.visibility = View.GONE
            holder.inboxName.setTypeface(null, Typeface.NORMAL)
        } else {
            holder.notification_icon.visibility = View.VISIBLE
            holder.inboxName.setTypeface(null, Typeface.BOLD)
        }
    }

    class InboxViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var inboxName: TextView = itemView.findViewById(R.id.inbox_item_name)
        var inboxDate: TextView = itemView.findViewById(R.id.inbox_item_date)
        var notification_icon: ImageView = itemView.findViewById(R.id.notification_icon)
    }
}