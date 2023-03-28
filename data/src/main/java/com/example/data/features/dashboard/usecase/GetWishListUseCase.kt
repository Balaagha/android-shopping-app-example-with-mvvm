package com.example.data.features.dashboard.usecase

import com.example.data.base.models.DataWrapper
import com.example.data.base.models.EmptyRequest
import com.example.data.base.usecase.BaseUseCaseForNetwork
import com.example.data.features.dashboard.models.CategoryModel
import com.example.data.features.dashboard.models.ProductModel
import com.example.data.features.dashboard.models.WishListResponse
import com.example.data.features.dashboard.repository.DashboardRepository
import com.example.data.features.entry.model.CustomerRequestModel
import com.example.data.features.entry.model.CustomerResponseModel
import com.example.data.features.entry.repository.EntryFlowRepository
import retrofit2.Response
import javax.inject.Inject

class GetWishListUseCase @Inject constructor(
    private val repository: DashboardRepository
) : BaseUseCaseForNetwork<WishListResponse, EmptyRequest>() {

    override suspend fun run(params: EmptyRequest): DataWrapper<Response<WishListResponse>> {
        return repository.getWishList()
    }

}