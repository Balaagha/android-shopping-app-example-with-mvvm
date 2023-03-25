package com.example.data.features.entry.services

import com.example.data.features.entry.model.CustomerLoginRequestModel
import com.example.data.features.entry.model.CustomerLoginResponseModel
import com.example.data.features.entry.model.CustomerRequestModel
import com.example.data.features.entry.model.CustomerResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface EntryFlowServices {

    @POST("api/customers")
    suspend fun createAccount(
        @Body requestData: CustomerRequestModel
    ): Response<CustomerResponseModel>

    @POST("api/customers/login")
    suspend fun loginAccount(
        @Body requestData: CustomerLoginRequestModel
    ): Response<CustomerLoginResponseModel>


}
