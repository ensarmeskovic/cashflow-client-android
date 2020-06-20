package com.example.cashflow.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cashflow.AppConstants.USER_LOGGED_IN
import com.example.cashflow.utils.SharedPreferencesUtil

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startHomeActivity()
    }

    private fun startHomeActivity() {
        val isUserLoggedIn = SharedPreferencesUtil.isUserLoggedIn(application)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(USER_LOGGED_IN, isUserLoggedIn)
        startActivity(intent)
        this.finish()
    }
}