package com.example.data.features.common.usecase

import com.example.data.base.models.DataWrapper
import com.example.data.base.usecase.BaseUseCaseForNetwork
import com.example.data.features.common.model.AddProductRequestModel
import com.example.data.features.common.model.AddProductResponseModel
import com.example.data.features.common.repository.CommonFlowRepository
import retrofit2.Response
import javax.inject.Inject

class UpdateProductsUseCase @Inject constructor(
    private val repository: CommonFlowRepository
) : BaseUseCaseForNetwork<AddProductResponseModel, AddProductRequestModel>() {

    override suspend fun run(params: AddProductRequestModel): DataWrapper<Response<AddProductResponseModel>> {
        return repository.updateProduct(params)
    }

}