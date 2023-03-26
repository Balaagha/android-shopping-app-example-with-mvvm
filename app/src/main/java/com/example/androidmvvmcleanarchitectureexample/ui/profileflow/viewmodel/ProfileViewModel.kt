package com.example.androidmvvmcleanarchitectureexample.ui.profileflow.viewmodel

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.example.androidmvvmcleanarchitectureexample.helper.toCustomerModelUiData
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.model.CustomerModelUiData
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.model.ProfileMenuItemType
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.model.ProfileMenuItemUiData
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.view.ProfileUpdateFragment
import com.example.core.viewmodel.BaseViewModel
import com.example.data.base.models.EmptyRequest
import com.example.data.features.entry.model.CustomerResponseModel
import com.example.data.features.entry.usecase.GetCustomerUseCase
import com.example.data.features.entry.usecase.UpdateCustomerUseCase
import com.example.data.helper.manager.UserDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCustomerUseCase: GetCustomerUseCase,
    private val updateCustomerUseCase: UpdateCustomerUseCase,
    private val userDataManager: UserDataManager,
    savedStateHandle: SavedStateHandle,
    private val applicationData: Application
) : BaseViewModel(
    savedStateHandle = savedStateHandle,
    application = applicationData
) {
    val menuUiModel by lazy {
        arrayListOf(
            ProfileMenuItemUiData(
                type = ProfileMenuItemType.ADDRESS,
                labelText = "Address"
            ),
            ProfileMenuItemUiData(
                type = ProfileMenuItemType.NOTIFICATION,
                labelText = "Notifications"
            ),
            ProfileMenuItemUiData(
                type = ProfileMenuItemType.PAYMENT,
                labelText = "Payment"
            ),
            ProfileMenuItemUiData(
                type = ProfileMenuItemType.SECURITY,
                labelText = "Security"
            ),
            ProfileMenuItemUiData(
                type = ProfileMenuItemType.PRIVACY,
                labelText = "Privacy policy"
            ),
            ProfileMenuItemUiData(
                type = ProfileMenuItemType.HELP_CENTER,
                labelText = "Help center"
            ),
            ProfileMenuItemUiData(
                type = ProfileMenuItemType.LOG_OUT,
                labelText = "Log out"
            )
        )
    }

    private val _profileUiData: MutableStateFlow<CustomerResponseModel?> = MutableStateFlow(null)
    val profileUiData = _profileUiData.asStateFlow()
    var userProfileUiData: CustomerModelUiData = CustomerModelUiData()

    fun getUserProfileData() {
        getCustomerUseCase.execute(
            EmptyRequest,
            successOperation = { customerData ->
                userProfileUiData =
                    customerData.invoke()?.toCustomerModelUiData() ?: CustomerModelUiData()
                _profileUiData.value = customerData.invoke()
            }
        )
    }

    fun updateUserProfile() {
        updateCustomerUseCase.execute(
            userProfileUiData.toCustomerRequestModelForUpdateCustomer(),
            successOperation = {
                userProfileUiData =
                    it.invoke()?.toCustomerModelUiData() ?: CustomerModelUiData()
                eventUiAction.postValue(ProfileUpdateFragment::class.java to GO_TO_PROFILE_SCREEN)
            })
    }

    fun clearUserData() {
        userDataManager.clearUserData()
    }

    companion object {
        const val GO_TO_PROFILE_SCREEN = "go_to_profile_screen"
    }

}
