package com.example.androidmvvmcleanarchitectureexample.ui.entryflow.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.model.CreateUserData
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.view.login.CreateAccountSecondFragment
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.view.login.LoginFragment
import com.example.core.viewmodel.BaseViewModel
import com.example.data.features.entry.usecase.CreateAccountUseCase
import com.example.data.features.entry.usecase.LoginAccountUseCase
import com.example.data.helper.manager.UserDataManager
import com.example.data.utils.SharedConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class EntryViewModel @Inject constructor(
    private val createAccountUseCase: CreateAccountUseCase,
    private val loginAccountUseCase: LoginAccountUseCase,
    private val userDataManager: UserDataManager,
    savedStateHandle: SavedStateHandle,
    private val applicationData: Application
) : BaseViewModel(
    savedStateHandle = savedStateHandle,
    application = applicationData
) {

    var createUserUiData: CreateUserData = CreateUserData()
    var loginUserUiData: CreateUserData = CreateUserData()

    var isRememberMeChecked: Boolean = false

    var isRegisteredUser: Boolean = false

    fun setIsRememberMeChecked(isChecked: Boolean) {
        isRememberMeChecked = isChecked
    }

    fun clearViewData() {
        createUserUiData = CreateUserData()
        loginUserUiData = CreateUserData()
        isRememberMeChecked = false
    }

    fun createUser() {
        createAccountUseCase.execute(
            createUserUiData.toCustomerRequestModel(),
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
                    eventUiAction.postValue(LoginFragment::class.java to GO_TO_PROFILE_SCREEN)
                } else {
                    eventUiAction.postValue(LoginFragment::class.java to GO_TO_DASHBOARD)
                }
            }
        )
    }

    companion object {
        const val ZERO = 0
        const val GO_TO_PROFILE_SCREEN = "go_to_profile_screen"
        const val GO_TO_DASHBOARD = "go_to_dashboard"
        const val GO_TO_LOGIN = "go_to_login"
        const val CLEAR_IS_REMEMBER_USER_DATA = "clear_is_remember_user_data"
    }

}
