package com.example.androidmvvmcleanarchitectureexample.helper

import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.model.CustomerModelUiData
import com.example.data.features.entry.model.CustomerResponseModel

fun CustomerResponseModel.toCustomerModelUiData(): CustomerModelUiData {
    return CustomerModelUiData().also { data ->
        data.email.set(this.email)
        data.firstName.set(this.firstName)
        data.lastName.set(this.lastName)
        data.userName.set(this.login)
        data.userNameForPath = this.login ?: ""
        data.gender.set(this.gender)
        data.telephone.set(this.telephone!!.take(7))
        data.telephone.set(this.telephone?.takeLast(7) ?: "")
        data.avatarUrl.set(this.avatarUrl)
    }
}