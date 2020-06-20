package com.example.cashflow.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.cashflow.AppConstants.EMPTY_STRING
import com.example.cashflow.activities.MainActivity
import com.example.cashflow.callbacks.InsertDialogCallback
import com.example.cashflow.navigation.NavigationHelper
import com.example.cashflow.utils.SharedPreferencesUtil
import com.example.cashflow.views.DialogError
import com.example.cashflow.views.DialogError.DialogErrorType
import com.example.cashflow.views.DialogInput


open class FragmentBase : Fragment() {
    protected val authToken: String by lazy {
        SharedPreferencesUtil.getAuthenticationToken(activity?.application)
            ?: EMPTY_STRING
    }

    protected lateinit var navigationHelper: NavigationHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentManager = fragmentManager ?: return
        val activity = (activity as MainActivity) ?: return
        navigationHelper = NavigationHelper(fragmentManager, activity)
    }

    protected fun showErrorDialog(dialogError: DialogErrorType) {
        val activity = activity ?: return
        DialogError(activity, dialogError).show()
    }

    protected fun showInsertInboxDialog(callback: InsertDialogCallback) {
        val activity = activity ?: return
        DialogInput(activity, callback).show()
    }

    protected fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    protected open fun initializeObservers() {}
    protected open fun setUpBinding() {}
}