package com.example.cashflow.fragments.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cashflow.AppConstants.HTTP_CODE_500
import com.example.cashflow.api.API
import com.example.cashflow.api.models.Authentication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {

    private val _authentication: MutableLiveData<Authentication.Response> = MutableLiveData()
    val authentication: LiveData<Authentication.Response>
        get() = _authentication

    private val _authenticationError: MutableLiveData<Int> = MutableLiveData()
    val authenticationError: LiveData<Int>
        get() = _authenticationError

    fun authenticateUser(authenticationRequest: Authentication.Request) {
        API.getService()?.authenticateMember(authenticationRequest)
            ?.enqueue(object : Callback<Authentication.Response> {
                override fun onFailure(
                    call: Call<Authentication.Response>,
                    t: Throwable
                ) {
                    _authenticationError.value = HTTP_CODE_500
                }

                override fun onResponse(
                    call: Call<Authentication.Response>,
                    response: Response<Authentication.Response>
                ) {
                    if (response.isSuccessful) {
                        _authentication.value = response.body()
                    } else {
                        _authenticationError.value = response.code()
                    }
                }
            })
    }
}