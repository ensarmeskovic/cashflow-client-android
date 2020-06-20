package com.example.cashflow.fragments.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cashflow.AppConstants.HTTP_CODE_500
import com.example.cashflow.api.API
import com.example.cashflow.api.models.Registration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterRepository {
    private val _successfullyRegistered: MutableLiveData<Int> = MutableLiveData()
    val successfullyRegistered: LiveData<Int>
        get() = _successfullyRegistered

    fun registerUser(registerRequest: Registration.Request) {
        API.getService()?.registerMember(registerRequest)?.enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _successfullyRegistered.value = HTTP_CODE_500
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                _successfullyRegistered.value = response.code()
            }
        })
    }
}