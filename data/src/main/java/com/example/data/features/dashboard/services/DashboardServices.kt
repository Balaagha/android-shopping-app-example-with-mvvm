package com.example.data.features.dashboard.services

import com.example.data.features.dashboard.models.CategoryModel
import com.example.data.features.dashboard.models.ProductModel
import com.example.data.features.dashboard.models.ProductsContent
import com.example.data.features.dashboard.models.SearchRequest
import com.example.data.features.entry.model.CustomerLoginRequestModel
import com.example.data.features.entry.model.CustomerLoginResponseModel
import com.example.data.features.entry.model.CustomerRequestModel
import com.example.data.features.entry.model.CustomerResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DashboardServices {

    @GET("api/catalog")
    suspend fun getCategories(): Response<List<CategoryModel>>

    @GET("api/products/filter")
    suspend fun getProducts(
        @Query("perPage") perPage: Int,
        @Query("startPage") startPage: Int
    ): Response<ProductsContent>


    @GET("api/products/filter")
    suspend fun getProductsByCategoryId(
        @Query("categories") categories: String,
        @Query("perPage") perPage: Int,
        @Query("startPage") startPage: Int
    ): Response<ProductsContent>


    @POST("api/products/search")
    suspend fun searchProducts(
        @Body request: SearchRequest,
    ): Response<List<ProductModel>>

    @GET("api/products")
    suspend fun getProduct(
        @Query("itemNo") itemNo: String
    ): Response<List<ProductModel>>


}
