package com.example.data.features.dashboard.repository

import android.content.Context
import com.example.data.base.models.DataWrapper
import com.example.data.base.models.RequestWrapper
import com.example.data.base.repository.BaseRepository
import com.example.data.features.common.model.SampleResponseModel
import com.example.data.features.common.services.CommonFlowServices
import com.example.data.features.dashboard.models.*
import com.example.data.features.dashboard.services.DashboardServices
import com.example.data.features.entry.model.CustomerLoginRequestModel
import com.example.data.features.entry.model.CustomerLoginResponseModel
import com.example.data.features.entry.model.CustomerRequestModel
import com.example.data.features.entry.model.CustomerResponseModel
import com.example.data.features.entry.services.EntryFlowServices
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

interface DashboardRepository {

    suspend fun getCategories(): DataWrapper<Response<List<CategoryModel>>>

    suspend fun getProducts(requestData: ProductsRequest): DataWrapper<Response<ProductsContent>>

    suspend fun getProductsByCategoryId(requestData: ProductsRequest): DataWrapper<Response<ProductsContent>>

    suspend fun searchProducts(requestData: SearchRequest): DataWrapper<Response<List<ProductModel>>>

    suspend fun getProduct(requestData: ProductsRequest): DataWrapper<Response<List<ProductModel>>>

    suspend fun createOrder(requestData: CreateOrder): DataWrapper<Response<Void>>


    suspend fun getCart(): DataWrapper<Response<CartResponse>>

    suspend fun createCart(requestData: CreateCart): DataWrapper<Response<Void>>

    suspend fun addToCart(requestData: CartRequest): DataWrapper<Response<Void>>

}

@Singleton
class DashboardRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val services: DashboardServices,
) : BaseRepository(context),DashboardRepository {

    override suspend fun getCategories(): DataWrapper<Response<List<CategoryModel>>> {
        return launchApiCall {
            services.getCategories()
        }
    }

    override suspend fun getProducts(requestData: ProductsRequest): DataWrapper<Response<ProductsContent>> {
        return launchApiCall {
            services.getProducts(perPage = requestData.perPage!!, startPage = requestData.startPage!!)
        }
    }

    override suspend fun getProductsByCategoryId(requestData: ProductsRequest): DataWrapper<Response<ProductsContent>> {
        return launchApiCall {
            services.getProductsByCategoryId(perPage = requestData.perPage!!, startPage = requestData.startPage!!, categories = requestData.categories!!)
        }
    }

    override suspend fun searchProducts(requestData: SearchRequest): DataWrapper<Response<List<ProductModel>>> {
        return launchApiCall {
            services.searchProducts(requestData)
        }
    }

    override suspend fun getProduct(requestData: ProductsRequest): DataWrapper<Response<List<ProductModel>>> {
        return launchApiCall {
            services.getProduct(requestData.itemNo!!)
        }
    }

    override suspend fun createOrder(requestData: CreateOrder): DataWrapper<Response<Void>> {
        return launchApiCall {
            services.createOrder(requestData)
        }
    }

    override suspend fun getCart(): DataWrapper<Response<CartResponse>> {
        return launchApiCall {
            services.getCart()
        }
    }

    override suspend fun createCart(requestData: CreateCart): DataWrapper<Response<Void>> {
        return launchApiCall {
            services.createCart(requestData)
        }
    }

    override suspend fun addToCart(requestData: CartRequest): DataWrapper<Response<Void>> {
        return launchApiCall {
            services.addToCart(requestData.productId!!)
        }
    }

}