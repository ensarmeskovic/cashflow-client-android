package com.example.cashflow.fragments.entries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cashflow.AppConstants.HTTP_CODE_500
import com.example.cashflow.api.API
import com.example.cashflow.api.models.AddEntry
import com.example.cashflow.api.models.InboxUser
import com.example.cashflow.data.Entry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EntriesRepository {
    private val _entriesList: MutableLiveData<Entry?> = MutableLiveData()
    val entriesList: LiveData<Entry?>
        get() = _entriesList

    private val _successfullyInsertedUser: MutableLiveData<Int> = MutableLiveData()
    val successfullyInsertedUser: LiveData<Int>
        get() = _successfullyInsertedUser

    private val _successfullyRemovedUser: MutableLiveData<Int> = MutableLiveData()
    val successfullyRemovedUser: LiveData<Int>
        get() = _successfullyRemovedUser

    private val _successfullyInsertedExpense: MutableLiveData<Int> = MutableLiveData()
    val successfullyInsertedExpense: LiveData<Int>
        get() = _successfullyInsertedExpense

    private val _successfullyInsertedPayment: MutableLiveData<Int> = MutableLiveData()
    val successfullyInsertedPayment: LiveData<Int>
        get() = _successfullyInsertedPayment

    private val _successfullyRemovedExpense: MutableLiveData<Int> = MutableLiveData()
    val successfullyRemovedExpense: LiveData<Int>
        get() = _successfullyRemovedExpense

    private val _successfullyRemovedPayment: MutableLiveData<Int> = MutableLiveData()
    val successfullyRemovedPayment: LiveData<Int>
        get() = _successfullyRemovedPayment


    fun addInboxUser(authToken: String, request: InboxUser.Request) {
        API.getService().addInboxUser(authToken, request).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _successfullyInsertedUser.value = HTTP_CODE_500
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                _successfullyInsertedUser.value = response.code()
            }
        })
    }

    fun deleteInboxUser(authToken: String, request: InboxUser.Request) {
        API.getService().deleteInboxUser(authToken, request).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _successfullyRemovedUser.value = HTTP_CODE_500
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                _successfullyRemovedUser.value = response.code()
            }
        })
    }

    fun getEntries(authToken: String, inboxId: Long) {
        API.getService().getEntries(authToken, inboxId).enqueue(object : Callback<Entry> {
            override fun onFailure(call: Call<Entry>, t: Throwable) {
                _entriesList.value = null
            }

            override fun onResponse(call: Call<Entry>, response: Response<Entry>) {
                _entriesList.value = response.body()
            }
        })
    }

    fun addExpense(authToken: String, request: AddEntry.ExpenseRequest) {
        API.getService().addExpense(authToken, request).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _successfullyInsertedExpense.value = HTTP_CODE_500
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                _successfullyInsertedExpense.value = response.code()
            }
        })
    }

    fun addPayment(authToken: String, request: AddEntry.PaymentRequest) {
        API.getService().addPayment(authToken, request).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _successfullyInsertedPayment.value = HTTP_CODE_500
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                _successfullyInsertedPayment.value = response.code()
            }
        })
    }

    fun deleteExpense(authToken: String, expenseId: Long) {
        API.getService().deleteExpense(authToken, expenseId).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _successfullyRemovedExpense.value = HTTP_CODE_500
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                _successfullyRemovedExpense.value = response.code()
            }
        })
    }

    fun deletePayment(authToken: String, paymentId: Long) {
        API.getService().deletePayment(authToken, paymentId).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _successfullyRemovedPayment.value = HTTP_CODE_500
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                _successfullyRemovedPayment.value = response.code()
            }
        })
    }
}