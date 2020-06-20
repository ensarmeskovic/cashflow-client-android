package com.example.cashflow.api

import com.example.cashflow.api.models.*
import com.example.cashflow.data.Entry
import com.example.cashflow.data.Inbox
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("/user/login")
    fun authenticateMember(@Body authentication: Authentication.Request): Call<Authentication.Response>

    @POST("user/registration")
    fun registerMember(@Body registrationRequest: Registration.Request): Call<Unit>

    @GET("inbox")
    fun getInboxData(@Header("Authentication") authenticationToken: String): Call<ArrayList<Inbox>>

    @POST("inbox/add")
    fun insertInboxData(@Header("Authentication") authenticationToken: String,
                        @Body name: AddInbox.Request): Call<Long>

    @DELETE("inbox/delete")
    fun deleteInbox(@Header("Authentication") authenticationToken: String,
                    @Query("id") inboxId: Long): Call<Unit>

    @POST("/user/change-password")
    fun changePassword(@Header("Authentication") authenticationToken: String,
                       @Body changePasswordRequest: ChangePassword.Request): Call<Unit>

    @GET("/user/logout")
    fun logout(@Header("Authentication") authenticationToken: String): Call<Unit>

    @GET("/inboxuser/seen")
    fun seenInbox(@Header("Authentication") authenticationToken: String,
                  @Query("inboxId") inboxId: Long): Call<Unit>

    @POST("/inbox/edit")
    fun editInbox(@Header("Authentication") authenticationToken: String,
                  @Body editInboxRequest: EditInbox.Request): Call<Unit>

    @PUT("/inbox/de-activate")
    fun deactivateInbox(@Header("authentication") authenticationToken: String,
                        @Query("id") inboxId: Long): Call<Unit>

    @POST("/inboxuser/add")
    fun addInboxUser(@Header("Authentication") authenticationToken: String,
                     @Body request: InboxUser.Request): Call<Unit>


    @HTTP(method = "DELETE", path = "/inboxuser/delete", hasBody = true)
    fun deleteInboxUser(@Header("Authentication") authenticationToken: String,
                     @Body request: InboxUser.Request): Call<Unit>

    @GET("/entry")
    fun getEntries(@Header("Authentication") authenticationToken: String,
                   @Query("id") inboxId: Long): Call<Entry>

    @POST("/expense/add")
    fun addExpense(@Header("Authentication") authenticationToken: String,
                   @Body request: AddEntry.ExpenseRequest): Call<Unit>

    @POST("/payment/add")
    fun addPayment(@Header("Authentication") authenticationToken: String,
                   @Body request: AddEntry.PaymentRequest): Call<Unit>

    @DELETE("/expense/delete")
    fun deleteExpense(@Header("Authentication") authenticationToken: String,
                      @Query("id") expenseId: Long): Call<Unit>

    @DELETE("/payment/delete")
    fun deletePayment(@Header("Authentication") authenticationToken: String,
                      @Query("id") expenseId: Long): Call<Unit>
}