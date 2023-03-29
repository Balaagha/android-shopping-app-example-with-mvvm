package com.example.data.features.dashboard.services

import com.example.data.features.dashboard.models.*
import com.example.data.features.dashboard.models.deletecard.DeleteCardResponse
import com.example.data.features.dashboard.models.getcard.CardResponseData
import com.example.data.features.dashboard.models.updatecatd.UpdateCardRequestModel
import com.example.data.features.entry.model.CustomerLoginRequestModel
import com.example.data.features.entry.model.CustomerLoginResponseModel
import com.example.data.features.entry.model.CustomerRequestModel
import com.example.data.features.entry.model.CustomerResponseModel
import retrofit2.Response
import retrofit2.http.*

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
        @Query("categories") categories: String? = null,
        @Query("perPage") perPage: Int,
        @Query("startPage") startPage: Int
    ): Response<ProductsContent>


    @POST("api/products/search")
    suspend fun searchProducts(
        @Body request: SearchRequest
    ): Response<List<ProductModel>>

    @GET("api/products")
    suspend fun getProduct(
        @Query("itemNo") itemNo: String
    ): Response<List<ProductModel>>


    @POST("api/orders")
    suspend fun createOrder(
        @Body request: CreateOrder
    ): Response<Any>

    @GET("api/cart")
    suspend fun getCart(): Response<CardResponseData>

    @DELETE("api/cart")
    suspend fun deleteCart(): Response<DeleteCardResponse>

    @POST("api/cart")
    suspend fun createCart(
        @Body request: CreateCart
    ): Response<Any>

    @PUT("api/cart")
    suspend fun addToCart(
        @Query("productId") productId: String
    ): Response<Any>

    @PUT("api/cart")
    suspend fun updateCart(
        @Body request: UpdateCardRequestModel
    ): Response<CardResponseData>


    @GET("api/wishlist")
    suspend fun getWishList(): Response<WishListResponse>

    @PUT("api/wishlist")
    suspend fun addProductToWishList(
        @Query("productId") productId: String
    ): Response<Any>

    @DELETE("api/wishlist")
    suspend fun deleteProductFromWishList(
        @Query("productId") productId: String
    ): Response<Any>

    @POST("api/wishlist")
    suspend fun createWishList(
        @Body request: CreateCart
    ): Response<Any>

}
