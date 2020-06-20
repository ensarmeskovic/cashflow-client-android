package com.example.cashflow.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cashflow.AppConstants.USER_LOGGED_IN
import com.example.cashflow.R
import com.example.cashflow.navigation.Navigation
import com.example.cashflow.navigation.NavigationHelper

class MainActivity : AppCompatActivity() {

    private val navigationHelper by lazy {
        NavigationHelper(supportFragmentManager, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val isLoggedIn = intent.getBooleanExtra(USER_LOGGED_IN, false)
        if (isLoggedIn) {
            navigationHelper.showScreen(Navigation.HOME)
        } else {
            navigationHelper.showScreen(Navigation.LOGIN)
        }
    }
}
