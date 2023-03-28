package com.example.androidmvvmcleanarchitectureexample.ui.profileflow.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.example.androidmvvmcleanarchitectureexample.helper.toCustomerModelUiData
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.model.CustomerModelUiData
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.model.AddProductModelUiData
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.model.ProfileMenuItemType
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.model.ProfileMenuItemUiData
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.view.AddProductFragment
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.view.ProfileUpdateFragment
import com.example.core.viewmodel.BaseViewModel
import com.example.data.base.models.EmptyRequest
import com.example.data.features.common.model.AddProductRequestModel
import com.example.data.features.common.usecase.AddProductsUseCase
import com.example.data.features.common.usecase.UpdateProductsUseCase
import com.example.data.features.dashboard.models.CategoryModel
import com.example.data.features.dashboard.models.ProductModel
import com.example.data.features.dashboard.models.ProductsRequest
import com.example.data.features.dashboard.usecase.GetCategoriesUseCase
import com.example.data.features.dashboard.usecase.GetProductsUseCase
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
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addProductsUseCase: AddProductsUseCase,
    private val updateProductsUseCase: UpdateProductsUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val userDataManager: UserDataManager,
    savedStateHandle: SavedStateHandle,
    private val applicationData: Application
) : BaseViewModel(
    savedStateHandle = savedStateHandle,
    application = applicationData
) {
    private val _productUiData: MutableStateFlow<List<ProductModel>?> = MutableStateFlow(null)
    val productUiData = _productUiData.asStateFlow()

    var page = 1

    fun getProducts() {
        val request = ProductsRequest(perPage = 20, startPage = page)
        getProductsUseCase.execute(
            params = request,
            successOperation = { responseData ->
                responseData.invoke()?.products?.let {
                    Log.d("MyProductListFragment", "it => $it in getProducts ")
                    _productUiData.value = _productUiData.value?.plus(it) ?: it
                }
            },
            isShowLoadingDialog = page == 1,
        )
    }

    fun clearProductList() {
        page = 1
        _productUiData.value = null
    }

    fun updateProductStatus(enabled: Boolean?, productId: String?, name: String?) {
        updateProductsUseCase.execute(AddProductRequestModel(
            enabled = enabled?.not(),
            id = productId,
            name = name
        ), successOperation = {
            _productUiData.value = _productUiData.value?.map {
                if (it._id == productId && enabled != null) {
                    it.copy(enabled = enabled.not())
                } else {
                    it
                }
            }
        })

    }

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

    private val _categoryListUiData: MutableStateFlow<List<CategoryModel>?> = MutableStateFlow(null)
    val categoryListUiData = _categoryListUiData.asStateFlow()

    private val _profileUiData: MutableStateFlow<CustomerResponseModel?> = MutableStateFlow(null)
    val profileUiData = _profileUiData.asStateFlow()

    var userProfileUiData: CustomerModelUiData = CustomerModelUiData()

    var addProductModelUiData: AddProductModelUiData = AddProductModelUiData()
    fun addImageToImageListForAddingProduct(imageUrl: Uri?) {
        imageUrl?.let {
            this.addProductModelUiData.imageUrls.add(it.toString())
        }
        Log.d("TAG", "addImageToImageListForAddingProduct: ${this.addProductModelUiData.imageUrls}")
    }

    fun addCategorySelection(category: CategoryModel) {
        this.addProductModelUiData.categories = category.name
    }

    fun checkIfUserSelectCategoryForAddingProduct(): Boolean {
        return this.addProductModelUiData.categories?.isNotEmpty() == true
    }

    fun checkIfUserAddImageForAddingProduct(): Boolean {
        return this.addProductModelUiData.imageUrls.isNotEmpty()
    }

    fun addProduct() {
        addProductsUseCase.execute(
            addProductModelUiData.toAddProductRequestModel(),
            successOperation = {
                eventUiAction.postValue(AddProductFragment::class.java to GO_TO_PROFILE_SCREEN)
            }
        )
    }

    fun clearAddedProductData() {
        this.addProductModelUiData = AddProductModelUiData()
    }

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

    fun getCategoryListData() {
        if (categoryListUiData.value != null) return
        getCategoriesUseCase.execute(
            EmptyRequest,
            successOperation = {
                _categoryListUiData.value = it.invoke()
            }
        )
    }

    companion object {
        const val GO_TO_PROFILE_SCREEN = "go_to_profile_screen"
    }

}
