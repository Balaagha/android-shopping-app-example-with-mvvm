package com.example.data.features.entry.usecase

import com.example.data.base.models.DataWrapper
import com.example.data.base.usecase.BaseUseCaseForNetwork
import com.example.data.features.entry.model.CustomerRequestModel
import com.example.data.features.entry.model.CustomerResponseModel
import com.example.data.features.entry.repository.EntryFlowRepository
import retrofit2.Response
import javax.inject.Inject

class UpdateCustomerUseCase @Inject constructor(
    private val repository: EntryFlowRepository
) : BaseUseCaseForNetwork<CustomerResponseModel, CustomerRequestModel>() {

    override suspend fun run(params: CustomerRequestModel): DataWrapper<Response<CustomerResponseModel>> {
        return repository.updateCustomerData(params)
    }

}