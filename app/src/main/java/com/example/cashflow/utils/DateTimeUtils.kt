package com.example.cashflow.utils

import android.content.Context
import android.text.format.DateUtils
import com.example.cashflow.R
import java.text.SimpleDateFormat

import java.util.*


object DateTimeUtils {

    fun getFormattedInboxDate(context: Context, d: Date): String {
        if (DateUtils.isToday(d.time)) { // today
            return context.getString(R.string.today)
        }

        if (DateUtils.isToday(d.time + DateUtils.DAY_IN_MILLIS)) { // yesterday
            return context.getString(R.string.yesterday)
        }

        if (DateUtils.isToday(d.time + (DateUtils.DAY_IN_MILLIS * 2))) { // two days ago
            return context.getString(R.string.two_days_ago)
        }

        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        return sdf.format(d)
    }
}