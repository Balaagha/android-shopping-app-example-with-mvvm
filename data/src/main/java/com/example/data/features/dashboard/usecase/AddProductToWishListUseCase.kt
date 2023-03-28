package com.example.data.features.dashboard.usecase

import com.example.data.base.models.DataWrapper
import com.example.data.base.models.EmptyRequest
import com.example.data.base.models.FailureBehavior
import com.example.data.base.models.FailureType
import com.example.data.base.usecase.BaseUseCaseForNetwork
import com.example.data.features.dashboard.models.*
import com.example.data.features.dashboard.repository.DashboardRepository
import com.example.data.features.entry.model.CustomerRequestModel
import com.example.data.features.entry.model.CustomerResponseModel
import com.example.data.features.entry.repository.EntryFlowRepository
import retrofit2.Response
import javax.inject.Inject

class AddProductToWishListUseCase @Inject constructor(
    private val repository: DashboardRepository
) : BaseUseCaseForNetwork<Any, WishListRequest>() {

    override suspend fun run(params: WishListRequest): DataWrapper<Response<Any>> {
        return repository.addProductToWishList(params)
    }

    override fun parseApiResponse(data: DataWrapper<Response<Any>>): DataWrapper<Any> {
        return when (data) {
            is DataWrapper.Failure -> {
                DataWrapper.Failure(
                    failureType = FailureType.EMPTY_OR_NULL_RESULT,
                    failureBehavior = FailureBehavior.SILENT
                )
            }
            else -> {super.parseApiResponse(data)}
        }
    }

}