package com.example.androidmvvmcleanarchitectureexample.ui.profileflow.view

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentAddProductBinding
import com.example.androidmvvmcleanarchitectureexample.databinding.GenericRvAddProductCategoryItemBinding
import com.example.androidmvvmcleanarchitectureexample.databinding.GenericRvProfileMenuItemBinding
import com.example.androidmvvmcleanarchitectureexample.helper.util.InputTextWatcherDelegate
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.activity.LoginActivity
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.model.ProfileMenuItemType
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.model.ProfileMenuItemUiData
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.viewmodel.ProfileViewModel
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.viewmodel.ProfileViewModel.Companion.GO_TO_PROFILE_SCREEN
import com.example.common.adapters.genericadapter.GenericAdapter
import com.example.common.utils.extentions.asColorInt
import com.example.core.view.BaseMvvmFragment
import com.example.data.features.dashboard.models.CategoryModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_product.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddProductFragment :
    BaseMvvmFragment<FragmentAddProductBinding, ProfileViewModel>(
        R.layout.fragment_add_product, ProfileViewModel::class
    ) {

    override val viewModelFactoryOwner: () -> ViewModelStoreOwner = {
        findNavController().getViewModelStoreOwner(R.id.nav_graph_profile)
    }

    private val inputTextWatcherDelegateList: ArrayList<InputTextWatcherDelegate> = ArrayList()

    private val storageRef by lazy {
        Firebase.storage.reference
    }

    private val activityForResultForImageOne =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intentResult = result.data
                handleImageResultData(
                    intentResult,
                    binding.imgProductOne,
                    binding.imgEmptyProductOne
                )
            }
        }

    private val activityForResultForImageTwo =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intentResult = result.data
                handleImageResultData(
                    intentResult,
                    binding.imgProductTwo,
                    binding.imgEmptyProductTwo
                )
            }
        }

    private val activityForResultForImageThree =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intentResult = result.data
                handleImageResultData(
                    intentResult,
                    binding.imgProductThree,
                    binding.imgEmptyProductThree
                )
            }
        }

    private val mCategoryAdapter by lazy {
        GenericAdapter<CategoryModel>(requireContext())
    }

    private fun handleImageResultData(
        intentResult: Intent?,
        imgProductOne: AppCompatImageView,
        imgEmptyProduct: AppCompatImageView
    ) {
        val profileImagesRef =
            storageRef.child("images/product/${uniqueFileName("product-image")}.jpg")
        viewModel.loadingEvent.postValue(true)
        imgEmptyProduct.isVisible = false
        profileImagesRef.putFile(intentResult?.data!!).addOnSuccessListener { mTask ->
            mTask.metadata?.reference?.downloadUrl?.addOnSuccessListener { imageUrl ->
                viewModel.userProfileUiData.avatarUrl.set(imageUrl.toString())
                Glide.with(requireContext()).load("$imageUrl")
                    .into(imgProductOne)
                imgProductOne.isVisible = true
                viewModel.addImageToImageListForAddingProduct(imageUrl)
                checkInputsValidationState()
                viewModel.loadingEvent.postValue(false)
            }?.addOnFailureListener {
                viewModel.loadingEvent.postValue(false)
                imgEmptyProduct.isVisible = true
            }
        }.addOnFailureListener {
            viewModel.loadingEvent.postValue(false)
            imgEmptyProduct.isVisible = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListener()
        binding.viewmodel = viewModel
        initMenuAdapter()
        initViewObserver()
    }

    override fun performOnViewCreatedOnlyOnce() {
        viewModel.getCategoryListData()
    }

    override fun handleUiActionEvent(action: String?) {
        when (action) {
            GO_TO_PROFILE_SCREEN -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun initViewObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoryListUiData.collect {
                    if (it != null) {
                        Log.d("AddProductFragment", "viewModel.categoryListUiData: is collected with data => $it  | size => ${it?.size}")
                        mCategoryAdapter.setData(
                            list = it,
                            notifyFunc = { mAdapter ->
                                mAdapter.notifyDataSetChanged()
                            }
                        )
                    }
                }
            }
        }
    }

    private fun initClickListener() {
        with(binding) {
            btnContinue.setOnClickListener {
                viewModel.addProduct()
            }
            expandableCategoryView.setOnClickListener {
                expand()
            }
            imgEmptyProductOne.setOnClickListener {
                activityForResultForImageOne.launch(getFileChooserIntentForImage())
            }
            imgEmptyProductTwo.setOnClickListener {
                activityForResultForImageTwo.launch(getFileChooserIntentForImage())
            }
            imgEmptyProductThree.setOnClickListener {
                activityForResultForImageThree.launch(getFileChooserIntentForImage())
            }
            navigationBackBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            inputTextWatcherDelegateList.clear()
            listOf(
                eTvName,
                eTvCurrentPrice,
                eTvPreviousPrice,
                eTvDescription,
                eTvQuantity
            ).forEach {
                val textWatcherDelegate = InputTextWatcherDelegate(
                    inputLayout = it,
                    minTextLength = 1,
                    maxTextLength = if (it == eTvDescription) 150 else 50,
                    afterTextChangedBlock = { value, isValid ->
                        btnContinue.isEnabled = true
                        checkInputsValidationState()
                    },
                )
                inputTextWatcherDelegateList.add(textWatcherDelegate)
                it.editText?.addTextChangedListener(textWatcherDelegate)
            }
            checkInputsValidationState()
        }
    }

    private fun initMenuAdapter() {
        mCategoryAdapter.expressionViewHolderBinding =
            { item, viewType, isAlreadyRendered, viewBinding ->
                val itemView = viewBinding as GenericRvAddProductCategoryItemBinding
                with(itemView) {
                    Log.d("AddProductFragment", "initMenuAdapter: item => $item | item.name => ${item.name}")
                    tvTitle.setViewText(item.name)
                    tvTitle.setViewVisibleState(true)
                    root.setOnClickListener {
                        viewModel.addCategorySelection(item)
                        tvCategoryTitle.setViewText(item.name)
                        expand(false)
                        checkInputsValidationState()
                    }
                }
            }
        mCategoryAdapter.expressionOnCreateViewHolder = { viewGroup, viewType ->
            GenericRvAddProductCategoryItemBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        }
        mCategoryAdapter.expressionOnGetItemViewType = { _, position ->
            position
        }
        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        with(binding.rvCategoryList) {
            layoutManager = manager
            adapter = mCategoryAdapter
        }

    }

    private fun expand(isExpand: Boolean? = null) {
        binding.apply {
            TransitionManager.beginDelayedTransition(pageWrapper, AutoTransition())
            isExpand?.let {
                rvCategoryList.isVisible = isExpand
            } ?: run {
                rvCategoryList.isVisible = rvCategoryList.isVisible.not()
            }
        }
    }

    private fun uniqueFileName(originalName: String): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return "file_$timestamp$originalName"
    }

    private fun checkInputsValidationState() {
        var isValid = true
        inputTextWatcherDelegateList.forEach {
            if (it.isValid().not()) {
                isValid = false
                return@forEach
            }
        }
        if (isValid) {
            isValid = viewModel.checkIfUserAddImageForAddingProduct() && viewModel.checkIfUserSelectCategoryForAddingProduct()
        }
        binding.btnContinue.isEnabled = isValid
    }

    private fun getFileChooserIntentForImage(): Intent {
        val STRING_FOR_INTENT_ONLY_IMAGE = "image/*"

        val mimeTypes = arrayOf(STRING_FOR_INTENT_ONLY_IMAGE)
        return Intent(Intent.ACTION_GET_CONTENT)
            .setType("${STRING_FOR_INTENT_ONLY_IMAGE}}")
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
    }
}