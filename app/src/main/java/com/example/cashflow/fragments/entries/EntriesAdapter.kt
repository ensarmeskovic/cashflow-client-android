package com.example.cashflow.fragments.entries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cashflow.AppConstants.EMPTY_STRING
import com.example.cashflow.R
import com.example.cashflow.data.EntryDetails

class EntriesAdapter : RecyclerView.Adapter<EntriesAdapter.EntryViewHolder>() {

    private val entryList: ArrayList<EntryDetails> = arrayListOf()

    fun setEntryList(entryList: List<EntryDetails>) {
        this.entryList.clear()
        this.entryList.addAll(entryList)
        notifyDataSetChanged()
    }

    fun getEntryList(): ArrayList<EntryDetails> {
        return entryList
    }

    fun getEntryByAdapterPosition(position: Int): Pair<Long?, Boolean> {
        val id = entryList[position].paymentId
        if (id != null) {
            return Pair(id, true)
        }
        val alterId = entryList[position].expenseId
        if (alterId != null) {
            return Pair(alterId, false)
        }
        return Pair(null, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.entry_item, parent, false)
        return EntryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return entryList.size
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entry = entryList[position]

        val entryName = entry.name ?: EMPTY_STRING
        holder.entryEmail.text = entry.username ?: EMPTY_STRING
        holder.entryAmount.text = entry.totalAmount?.toString()
        if (entry.paymentId == null) {
            holder.entryName.text = entryName
        } else {
            holder.entryName.text =
                holder.itemView.context?.resources?.getString(R.string.payment_label)
                    ?: EMPTY_STRING
        }

    }

    class EntryViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var entryName: TextView = itemView.findViewById(R.id.entry_item_name)
        var entryEmail: TextView = itemView.findViewById(R.id.entry_item_email)
        var entryAmount: TextView = itemView.findViewById(R.id.entry_amount)
    }
}