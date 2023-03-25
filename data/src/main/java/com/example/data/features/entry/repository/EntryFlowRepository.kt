package com.example.data.features.entry.repository

import android.content.Context
import com.example.data.base.models.DataWrapper
import com.example.data.base.models.RequestWrapper
import com.example.data.base.repository.BaseRepository
import com.example.data.features.common.model.SampleResponseModel
import com.example.data.features.common.services.CommonFlowServices
import com.example.data.features.entry.model.CustomerLoginRequestModel
import com.example.data.features.entry.model.CustomerLoginResponseModel
import com.example.data.features.entry.model.CustomerRequestModel
import com.example.data.features.entry.model.CustomerResponseModel
import com.example.data.features.entry.services.EntryFlowServices
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

interface EntryFlowRepository {

    suspend fun createAccount(requestData: CustomerRequestModel): DataWrapper<Response<CustomerResponseModel>>
    suspend fun loginAccount(requestData: CustomerLoginRequestModel): DataWrapper<Response<CustomerLoginResponseModel>>

}

@Singleton
class EntryFlowRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val services: EntryFlowServices,
) : BaseRepository(context),EntryFlowRepository {

    override suspend fun createAccount(requestData: CustomerRequestModel): DataWrapper<Response<CustomerResponseModel>> {
        return launchApiCall {
            services.createAccount(
                requestData = requestData
            )
        }
    }

    override suspend fun loginAccount(requestData: CustomerLoginRequestModel): DataWrapper<Response<CustomerLoginResponseModel>> {
        return launchApiCall {
            services.loginAccount(
                requestData = requestData
            )
        }
    }

}