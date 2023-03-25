package com.example.androidmvvmcleanarchitectureexample.ui.entryflow.model

import androidx.databinding.ObservableField
import com.example.data.features.entry.model.CustomerLoginRequestModel
import com.example.data.features.entry.model.CustomerRequestModel

class CreateUserData(
    val email: ObservableField<String> = ObservableField(),
    val firstName: ObservableField<String> = ObservableField(),
    val lastName: ObservableField<String> = ObservableField(),
    val userName: ObservableField<String> = ObservableField(),
    val password: ObservableField<String> = ObservableField(),
    val gender: ObservableField<String> = ObservableField(),
    val telephone: ObservableField<String> = ObservableField()
) {
    fun toCustomerRequestModel(): CustomerRequestModel = CustomerRequestModel(
        email = email.get(),
        userName = userName.get(),
        firstName = firstName.get(),
        lastName = lastName.get(),
        password = password.get()
    )

    fun toCustomerLoginRequestModel(): CustomerLoginRequestModel = CustomerLoginRequestModel(
        email = email.get(),
        password = password.get()
    )
}