package com.example.cashflow.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.cashflow.AppConstants.EMPTY_STRING

object SharedPreferencesUtil {
    private const val CASH_FLOW_SHARED_PREFERENCES = "CashFlowSharedPreferences"
    private const val SHARED_PREFERENCES_IS_USER_LOGGED_IN = "SharedPreferencesIsUserLoggedIn"
    private const val SHARED_PREFERENCES_AUTHENTICACTION_TOKEN =
        "SharedPreferencesAuthenticationToke"

    fun isUserLoggedIn(application: Application): Boolean {
        val preferences: SharedPreferences = application.applicationContext
            .getSharedPreferences(CASH_FLOW_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        return preferences.getBoolean(SHARED_PREFERENCES_IS_USER_LOGGED_IN, false)
    }

    fun setUserLoggedIn(application: Application, loggedIn: Boolean) {
        application.applicationContext
            .getSharedPreferences(CASH_FLOW_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(SHARED_PREFERENCES_IS_USER_LOGGED_IN, loggedIn)
            .apply()
    }

    fun getAuthenticationToken(application: Application?): String? {
        application ?: return null
        val preferences: SharedPreferences = application.applicationContext
            .getSharedPreferences(CASH_FLOW_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        return preferences.getString(SHARED_PREFERENCES_AUTHENTICACTION_TOKEN, EMPTY_STRING)
    }

    fun setAuthenticationToken(application: Application, token: String) {
        application.applicationContext
            .getSharedPreferences(CASH_FLOW_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            .edit()
            .putString(SHARED_PREFERENCES_AUTHENTICACTION_TOKEN, token)
            .apply()
    }
}