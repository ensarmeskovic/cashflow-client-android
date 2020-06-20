package com.example.cashflow.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cashflow.AppConstants.EMPTY_STRING
import com.example.cashflow.AppConstants.HTTP_CODE_200
import com.example.cashflow.R
import com.example.cashflow.databinding.FragmentMenuBinding
import com.example.cashflow.fragments.FragmentBase
import com.example.cashflow.navigation.Navigation
import com.example.cashflow.utils.SharedPreferencesUtil.setAuthenticationToken
import com.example.cashflow.utils.SharedPreferencesUtil.setUserLoggedIn

class FragmentMenu : FragmentBase() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(MenuViewModel::class.java)
    }

    private val onPasswordChangeClickListener = View.OnClickListener {
        navigationHelper.showScreen(Navigation.CHANGE_PASSWORD)
    }

    private val onLogoutClickListener = View.OnClickListener {
        viewModel.menuRepository.logout(authToken)
        spinner.visibility = View.VISIBLE
    }

    private lateinit var dataBinding: FragmentMenuBinding

    // views
    private lateinit var spinner: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_menu, container, false
        )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeObservers()
        setUpBinding()
    }

    override fun initializeObservers() {
        viewModel.menuRepository.successfullyLoggedOut.observe(viewLifecycleOwner,
            Observer<Int> {
                spinner.visibility = View.GONE
                if (it == HTTP_CODE_200) {
                    handleLogout()
                } else {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.toast_logout_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    override fun setUpBinding() {
        spinner = dataBinding.progressBarContainer
        dataBinding.changePasswordButton.setOnClickListener(onPasswordChangeClickListener)
        dataBinding.logoutButton.setOnClickListener(onLogoutClickListener)
    }

    private fun handleLogout() {
        navigationHelper.showScreen(Navigation.LOGIN_AFTER_LOGOUT)
        val application = activity?.application ?: return
        setAuthenticationToken(application, EMPTY_STRING)
        setUserLoggedIn(application, false)
    }
}