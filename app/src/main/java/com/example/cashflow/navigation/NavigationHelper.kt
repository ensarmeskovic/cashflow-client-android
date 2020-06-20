package com.example.cashflow.navigation

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.cashflow.AppConstants
import com.example.cashflow.R
import com.example.cashflow.activities.MainActivity
import com.example.cashflow.data.Inbox
import com.example.cashflow.fragments.changePassword.FragmentChangePassword
import com.example.cashflow.fragments.entries.FragmentEntries
import com.example.cashflow.fragments.home.FragmentHome
import com.example.cashflow.fragments.login.FragmentLogin
import com.example.cashflow.fragments.menu.FragmentMenu
import com.example.cashflow.fragments.register.FragmentRegister


class NavigationHelper(
    private val fragmentManager: FragmentManager,
    private val currentActivity: Activity
) {

    fun showScreen(navigation: Navigation) {
        when (navigation) {
            Navigation.HOME -> showHomeScreen()
            Navigation.LOGIN -> showLoginScreen(false)
            Navigation.REGISTER -> showRegisterScreen()
            Navigation.MENU -> showMenuScreen()
            Navigation.LOGIN_AFTER_LOGOUT -> showLoginScreen(true)
            Navigation.CHANGE_PASSWORD -> changePassword()
        }
    }

    private fun showHomeScreen() {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(
            R.id.main_activity_container,
            FragmentHome(),
            FragmentHome::class.java.simpleName
        )
        applyCustomAnimations(transaction)
        transaction.commitAllowingStateLoss()
    }

    private fun showLoginScreen(afterLogout: Boolean) {
        if (afterLogout) {
            val intent = Intent(currentActivity, MainActivity::class.java)
            intent.putExtra(AppConstants.USER_LOGGED_IN, false)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            currentActivity.startActivity(intent)
            currentActivity.finish()
        } else {
            val transaction = fragmentManager.beginTransaction()
            applyCustomAnimations(transaction)
            transaction.replace(
                R.id.main_activity_container,
                FragmentLogin(), FragmentLogin::class.java.simpleName
            )
            transaction.commitAllowingStateLoss()
        }
    }

    private fun showRegisterScreen() {
        val transaction = fragmentManager.beginTransaction()
        applyCustomAnimations(transaction)
        transaction.add(
            R.id.main_activity_container,
            FragmentRegister(), FragmentRegister::class.java.simpleName
        )
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
    }

    private fun showMenuScreen() {
        val transaction = fragmentManager.beginTransaction()
        applyCustomAnimations(transaction)
        transaction.add(
            R.id.main_activity_container,
            FragmentMenu(), FragmentMenu::class.java.simpleName
        )
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
    }

    private fun changePassword() {
        val transaction = fragmentManager.beginTransaction()
        applyCustomAnimations(transaction)
        transaction.add(
            R.id.main_activity_container,
            FragmentChangePassword(), FragmentChangePassword::class.java.simpleName
        )
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
    }

    fun showEntriesScreen(inbox: Inbox?) {
        val transaction = fragmentManager.beginTransaction()
        applyCustomAnimations(transaction)
        val fragment = FragmentEntries()
        fragment.selectedInbox = inbox
        transaction.replace(
            R.id.main_activity_container,
            fragment, FragmentEntries::class.java.simpleName
        )
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
    }

    private fun applyCustomAnimations(transaction: FragmentTransaction) {
        transaction.setCustomAnimations(
            R.animator.fast_fade_in,
            R.animator.fade_out,
            R.animator.fast_fade_in,
            R.animator.fade_out
        )
    }

}