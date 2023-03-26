package com.example.data.features.dashboard.repository

import android.content.Context
import com.example.data.base.models.DataWrapper
import com.example.data.base.models.RequestWrapper
import com.example.data.base.repository.BaseRepository
import com.example.data.features.common.model.SampleResponseModel
import com.example.data.features.common.services.CommonFlowServices
import com.example.data.features.dashboard.models.CategoryModel
import com.example.data.features.dashboard.models.ProductsContent
import com.example.data.features.dashboard.models.ProductsRequest
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

}