package com.example.androidmvvmcleanarchitectureexample.ui.entryflow.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.example.androidmvvmcleanarchitectureexample.helper.toCustomerModelUiData
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.model.CustomerModelUiData
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.view.login.CreateAccountSecondFragment
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.view.login.LoginFragment
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.view.login.UserProfileFillFragment
import com.example.core.viewmodel.BaseViewModel
import com.example.data.base.models.EmptyRequest
import com.example.data.features.entry.usecase.CreateAccountUseCase
import com.example.data.features.entry.usecase.GetCustomerUseCase
import com.example.data.features.entry.usecase.LoginAccountUseCase
import com.example.data.features.entry.usecase.UpdateCustomerUseCase
import com.example.data.helper.manager.UserDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class EntryViewModel @Inject constructor(
    private val createAccountUseCase: CreateAccountUseCase,
    private val loginAccountUseCase: LoginAccountUseCase,
    private val getCustomerUseCase: GetCustomerUseCase,
    private val updateCustomerUseCase: UpdateCustomerUseCase,
    private val userDataManager: UserDataManager,
    savedStateHandle: SavedStateHandle,
    private val applicationData: Application
) : BaseViewModel(
    savedStateHandle = savedStateHandle,
    application = applicationData
) {

    var createUserUiData: CustomerModelUiData = CustomerModelUiData()
    var loginUserUiData: CustomerModelUiData = CustomerModelUiData()
    var userProfileUiData: CustomerModelUiData = CustomerModelUiData()

    var isRememberMeChecked: Boolean = false

    var isRegisteredUser: Boolean = false

    fun setIsRememberMeChecked(isChecked: Boolean) {
        isRememberMeChecked = isChecked
    }

    fun clearViewData() {
        createUserUiData = CustomerModelUiData()
        loginUserUiData = CustomerModelUiData()
        isRememberMeChecked = false
    }

    fun createUser() {
        createAccountUseCase.execute(
            createUserUiData.toCustomerRequestModelForCreateCustomer(),
            successOperation = {
                isRegisteredUser = true
                if (isRememberMeChecked) {
                    userDataManager.saveUserEmailAndPassword(
                        createUserUiData.email.get(),
                        createUserUiData.password.get(),
                        true
                    )
                } else {
                    userDataManager.saveUserEmailAndPassword("", "",false) // clear user data
                }
                eventUiAction.postValue(CreateAccountSecondFragment::class.java to GO_TO_LOGIN)
            }
        )
    }

    fun loginUser() {
        loginAccountUseCase.execute(
            loginUserUiData.toCustomerLoginRequestModel(),
            isDismissHideBaseLoadingIndicator = true,
            successOperation = {
                if (isRememberMeChecked) {
                    userDataManager.saveUserEmailAndPassword(
                        loginUserUiData.email.get(),
                        loginUserUiData.password.get(),
                        true
                    )
                } else {
                    userDataManager.saveUserEmailAndPassword("", "",false) // clear user data
                }
                userDataManager.saveApiToken(it.invoke()?.token,true)
                if (isRegisteredUser){
                    getCustomerUseCase.execute(
                        EmptyRequest,
                        successOperation = { customerData ->
                            try {
                                userProfileUiData = customerData.invoke()?.toCustomerModelUiData() ?: CustomerModelUiData()
                                eventUiAction.postValue(LoginFragment::class.java to GO_TO_PROFILE_SCREEN)
                            } catch (e: Exception){
                                eventUiAction.postValue(LoginFragment::class.java to GO_TO_DASHBOARD)
                            }
                        },failOperation = {
                            eventUiAction.postValue(LoginFragment::class.java to GO_TO_DASHBOARD)
                        }
                    )
                } else {
                    loadingEvent.postValue(false)
                    eventUiAction.postValue(LoginFragment::class.java to GO_TO_DASHBOARD)
                }
            }
        )
    }

    fun updateUserProfile() {
        updateCustomerUseCase.execute(userProfileUiData.toCustomerRequestModelForUpdateCustomer(),successOperation = {
            eventUiAction.postValue(UserProfileFillFragment::class.java to GO_TO_DASHBOARD)
        })
    }

    companion object {
        const val ZERO = 0
        const val GO_TO_PROFILE_SCREEN = "go_to_profile_screen"
        const val GO_TO_DASHBOARD = "go_to_dashboard"
        const val GO_TO_LOGIN = "go_to_login"
        const val CLEAR_IS_REMEMBER_USER_DATA = "clear_is_remember_user_data"
    }

}
