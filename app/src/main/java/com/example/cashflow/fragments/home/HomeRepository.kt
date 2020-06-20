package com.example.cashflow.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cashflow.api.API
import com.example.cashflow.api.models.AddInbox
import com.example.cashflow.api.models.EditInbox
import com.example.cashflow.callbacks.RequestCallback
import com.example.cashflow.data.Inbox
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepository {
    private val _inboxList: MutableLiveData<ArrayList<Inbox>> = MutableLiveData()
    val inboxList: LiveData<ArrayList<Inbox>>
        get() = _inboxList

    fun getInboxList(token: String) {
        API.getService().getInboxData(token).enqueue(object : Callback<ArrayList<Inbox>> {
            override fun onFailure(call: Call<ArrayList<Inbox>>, t: Throwable) {
                _inboxList.value = arrayListOf()
            }

            override fun onResponse(
                call: Call<ArrayList<Inbox>>,
                response: Response<ArrayList<Inbox>>
            ) {
                if (response.isSuccessful) {
                    _inboxList.value = response.body()
                } else {
                    _inboxList.value = arrayListOf()
                }
            }
        })
    }

    fun insertInbox(authToken: String, inboxRequest: AddInbox.Request, callback: RequestCallback) {
        API.getService().insertInboxData(authToken, inboxRequest).enqueue(object : Callback<Long> {
            override fun onFailure(call: Call<Long>, t: Throwable) {
                callback.onFailure()
            }

            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    callback.onSuccess()
                    val inboxId: Long = response.body() ?: return
                    seenInbox(authToken, inboxId)
                }
            }
        })
    }

    fun deleteInbox(authToken: String, inboxId: Long, callback: RequestCallback) {
        API.getService().deleteInbox(authToken, inboxId).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onFailure()
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                callback.onSuccess()
            }
        })
    }

    fun seenInbox(authToken: String, inboxId: Long) {
        API.getService().seenInbox(authToken, inboxId).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
            }
        })
    }

    fun editInbox(authToken: String, request: EditInbox.Request, callback: RequestCallback) {
        API.getService().editInbox(authToken, request).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onFailure()
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                callback.onSuccess()
            }
        })
    }

    fun deactivateInbox(authToken: String, inboxId: Long, callback: RequestCallback) {
        API.getService().deactivateInbox(authToken, inboxId).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onFailure()
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                callback.onSuccess()
            }
        })
    }
}