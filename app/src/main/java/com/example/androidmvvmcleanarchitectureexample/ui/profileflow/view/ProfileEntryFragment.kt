package com.example.androidmvvmcleanarchitectureexample.ui.profileflow.view

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
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
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentProfileEntryBinding
import com.example.androidmvvmcleanarchitectureexample.databinding.GenericRvProfileMenuItemBinding
import com.example.androidmvvmcleanarchitectureexample.ui.MainActivity
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.activity.LoginActivity
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.view.login.LoginFragment
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.model.ProfileMenuItemType
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.model.ProfileMenuItemUiData
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.viewmodel.ProfileViewModel
import com.example.common.adapters.genericadapter.GenericAdapter
import com.example.common.utils.extentions.asColorInt
import com.example.core.view.BaseMvvmFragment
import com.example.data.features.entry.model.CustomerResponseModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileEntryFragment : BaseMvvmFragment<FragmentProfileEntryBinding, ProfileViewModel>(
    R.layout.fragment_profile_entry, ProfileViewModel::class
) {

    private val mMenuAdapter by lazy {
        GenericAdapter<ProfileMenuItemUiData>(requireContext())
    }

    override val viewModelFactoryOwner: () -> ViewModelStoreOwner = {
        findNavController().getViewModelStoreOwner(R.id.nav_graph_profile)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMenuAdapter()
        initListeners()
        initViewObserver()
        Log.d("ProfileEntryFragment", "onViewCreated: is called"  )
    }

    override fun performOnViewCreatedOnlyOnce() {
        viewModel.getUserProfileData()
        mMenuAdapter.setData(
            list = viewModel.menuUiModel,
            notifyFunc = { mAdapter ->
                mAdapter.notifyDataSetChanged()
            }
        )
    }

    override fun performOnViewCreatedIfViewDestroyedAtLeastOnce() {
        viewModel.profileUiData.value?.let { setViewData(it) }
    }

    private fun initViewObserver() {
        Log.d("ProfileEntryFragment", "initViewObserver: is called"  )
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.profileUiData.collect {
                    Log.d("ProfileEntryFragment", "viewModel.profileUiData: is collected with data => $it")
                    if (it != null) {
                        setViewData(it)
                    }
                }
            }
        }
    }

    private fun setViewData(data: CustomerResponseModel) {
        with(binding) {
            Log.d("ProfileEntryFragment", "setViewData: is called data.avatarUrl => ${data.avatarUrl}"  )
            Glide.with(requireContext()).load(data.avatarUrl).placeholder(R.drawable.ic_user_profile_photo).error(R.drawable.ic_user_profile_photo).into(imgViewProfilePhoto)
            btnAddProduct.isVisible = data.isAdmin == true
            btnShowMyProducts.isVisible = data.isAdmin == true
        }
    }

    private fun initListeners() {
        with(binding) {
            navigationBackBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            btnEditProfilePhoto.setOnClickListener {
                findNavController().navigate(R.id.action_profileEntryFragment_to_profileUpdateFragment)
            }
            btnEditProfile.setOnClickListener {
                findNavController().navigate(R.id.action_profileEntryFragment_to_profileUpdateFragment)
            }
            btnShowMyProducts.setOnClickListener {
                viewModel.clearProductList()
                findNavController().navigate(R.id.action_profileEntryFragment_to_myProductListFragment)
            }
            btnAddProduct.setOnClickListener {
                viewModel.clearAddedProductData()
                findNavController().navigate(R.id.action_profileEntryFragment_to_addProductFragment)
            }


        }
    }

    private fun initMenuAdapter() {
        mMenuAdapter.expressionViewHolderBinding = { item, viewType, isAlreadyRendered, viewBinding ->
            val itemView = viewBinding as GenericRvProfileMenuItemBinding
            with(itemView){
                tvTitle.setViewText(item.labelText)
                tvTitle.setViewVisibleState(true)
                imgViewLeftIcon.setImageResource(item.type.iconResId)
                if(item.type == ProfileMenuItemType.LOG_OUT){
                    imgViewLeftIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorAccentCarroot), PorterDuff.Mode.SRC_IN);
                    tvTitle.setViewTextColor(R.color.colorAccentCarroot.asColorInt(requireContext()))
                }
                root.setOnClickListener {
                    when(item.type){
                        ProfileMenuItemType.LOG_OUT -> {
                            viewModel.clearUserData()
                            activity?.apply {
                                finish()
                                startActivity(Intent(requireContext(), LoginActivity::class.java))
                            }
                        }
                    }
                }
            }
        }
        mMenuAdapter.expressionOnCreateViewHolder = { viewGroup, viewType ->
            GenericRvProfileMenuItemBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        }
        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        with(binding.rvProfileMenu) {
            layoutManager = manager
            adapter = mMenuAdapter
        }

    }

}