package com.example.cashflow.fragments.changePassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cashflow.AppConstants.HTTP_CODE_500
import com.example.cashflow.api.API
import com.example.cashflow.api.models.ChangePassword
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordRepository {
    private val _successfullyChanged: MutableLiveData<Int> = MutableLiveData()
    val successfullyChanged: LiveData<Int>
        get() = _successfullyChanged

    fun changePassword(token: String, passwordRequest: ChangePassword.Request) {
        API.getService().changePassword(token, passwordRequest).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _successfullyChanged.value = HTTP_CODE_500
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                _successfullyChanged.value = response.code()
            }
        })
    }
}