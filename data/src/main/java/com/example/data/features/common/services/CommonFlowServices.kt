package com.example.data.features.common.services

import com.example.data.features.common.model.AddProductRequestModel
import com.example.data.features.common.model.AddProductResponseModel
import com.example.data.features.common.model.SampleResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.QueryMap

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


}
