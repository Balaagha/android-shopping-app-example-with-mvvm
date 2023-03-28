package com.example.data.features.common.repository

import android.content.Context
import com.example.data.base.models.DataWrapper
import com.example.data.base.repository.BaseRepository
import com.example.data.features.common.model.AddProductRequestModel
import com.example.data.features.common.model.AddProductResponseModel
import com.example.data.features.common.model.SampleResponseModel
import com.example.data.features.common.model.orders.GetOrdersResponseModel
import com.example.data.features.common.services.CommonFlowServices
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

interface CommonFlowRepository {

    suspend fun getSampleData(): DataWrapper<Response<SampleResponseModel>>
    suspend fun addProduct(requestData: AddProductRequestModel): DataWrapper<Response<AddProductResponseModel>>
    suspend fun updateProduct(requestData: AddProductRequestModel): DataWrapper<Response<AddProductResponseModel>>
    suspend fun getOrders(): DataWrapper<Response<GetOrdersResponseModel>>

}

@Singleton
class CommonFlowRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val services: CommonFlowServices,
) : BaseRepository(context),CommonFlowRepository {


    override suspend fun getSampleData(): DataWrapper<Response<SampleResponseModel>> {
        return launchApiCall {
            services.getSampleData(
                queries = hashMapOf(),
                headers = hashMapOf()
            )
        }
    }

    override suspend fun addProduct(requestData: AddProductRequestModel): DataWrapper<Response<AddProductResponseModel>> {
        return launchApiCall {
            services.addProducts(
                requestData = requestData
            )
        }
    }

    override suspend fun updateProduct(requestData: AddProductRequestModel): DataWrapper<Response<AddProductResponseModel>> {
        return launchApiCall {
            services.updateProducts(
                requestData = requestData,
                id = requestData.id
            )
        }
    }

    override suspend fun getOrders(): DataWrapper<Response<GetOrdersResponseModel>> {
        return launchApiCall {
            services.getOrders()
        }
    }

}