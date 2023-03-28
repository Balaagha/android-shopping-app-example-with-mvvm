package com.example.data.features.common.services

import com.example.data.features.common.model.AddProductRequestModel
import com.example.data.features.common.model.AddProductResponseModel
import com.example.data.features.common.model.SampleResponseModel
import com.example.data.features.common.model.orders.GetOrdersResponseModel
import retrofit2.Response
import retrofit2.http.*

interface CommonFlowServices {

    @POST("/url")
    suspend fun getSampleData(
        @QueryMap queries: HashMap<String, String>,
        @HeaderMap headers: HashMap<String, String>
    ): Response<SampleResponseModel>

    @POST("api/products")
    suspend fun addProducts(
        @Body requestData: AddProductRequestModel
    ): Response<AddProductResponseModel>

    @GET("api/orders")
    suspend fun getOrders(
    ): Response<GetOrdersResponseModel>

}
