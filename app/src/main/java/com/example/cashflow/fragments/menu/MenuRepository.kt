package com.example.cashflow.fragments.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cashflow.AppConstants.HTTP_CODE_500
import com.example.cashflow.api.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuRepository {
    private val _successfullyLoggedOut: MutableLiveData<Int> = MutableLiveData()
    val successfullyLoggedOut: LiveData<Int>
        get() = _successfullyLoggedOut

    fun logout(token: String) {
        API.getService().logout(token).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _successfullyLoggedOut.value = HTTP_CODE_500
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                _successfullyLoggedOut.value = response.code()
            }
        })
    }
}