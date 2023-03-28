package com.example.androidmvvmcleanarchitectureexample.ui.profileflow.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentMyProductListBinding
import com.example.androidmvvmcleanarchitectureexample.databinding.GenericRvMyProductItemBinding
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.viewmodel.ProfileViewModel
import com.example.common.adapters.genericadapter.GenericAdapter
import com.example.common.adapters.genericadapter.PaginationScrollListener
import com.example.common.utils.extentions.getIfExists
import com.example.core.view.BaseMvvmFragment
import com.example.data.features.dashboard.models.ProductModel
import com.example.uitoolkit.custom.models.ItemModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyProductListFragment :
    BaseMvvmFragment<FragmentMyProductListBinding, ProfileViewModel>(
        R.layout.fragment_my_product_list, ProfileViewModel::class
    ) {

    override val viewModelFactoryOwner: () -> ViewModelStoreOwner = {
        findNavController().getViewModelStoreOwner(R.id.nav_graph_profile)
    }

    private val mProductAdapter by lazy {
        GenericAdapter<ProductModel>(requireContext())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListener()
        initProductAdapter()
        initViewObserver()
    }

    override fun performOnViewCreatedOnlyOnce() {
        viewModel.getProducts()
    }

    private var isLoading = false
    private var isLastPage = false

    private fun initViewObserver() {
        Log.d("MyProductListFragment", "initViewObserver: called")

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                Log.d("MyProductListFragment", "in viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED)")
                viewModel.productUiData.collect {
                    Log.d("TAG", "initViewObserver: it?.size => ${it?.size} | it => $it")
                    if(it != null) {
                        if (it.size < 20) {
                            isLastPage = true
                        }
                        isLoading = false
                        binding.loading.visibility = View.GONE
                        mProductAdapter.setData(
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
            navigationBackBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initProductAdapter() {
        mProductAdapter.expressionViewHolderBinding =
            { item, viewType, isAlreadyRendered, viewBinding ->
                val itemView = viewBinding as GenericRvMyProductItemBinding
                with(itemView) {
                    title.setViewText(item.name)
                    subTitle.setViewText("US $${item.currentPrice}")
                    val productModel = ItemModel(
                        imageurl = item.imageUrls?.getIfExists(0)
                    )
                    productView.setViewData(productModel)
                    if (item.enabled == true) {
                        btnDeactivate.text = "Deactivate"
                    } else {
                        btnDeactivate.text = "Inactive"
                    }
                    btnDeactivate.setOnClickListener {
                        viewModel.updateProductStatus(item.enabled,item._id,item.name)
                    }
                }
            }
        mProductAdapter.expressionOnCreateViewHolder = { viewGroup, viewType ->
            GenericRvMyProductItemBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        }
        mProductAdapter.expressionOnGetItemViewType = { _, position ->
            position
        }
        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        with(binding.rvProductList) {
            layoutManager = manager
            adapter = mProductAdapter
            setUpScrollListener(manager)
        }

    }

    private fun setUpScrollListener(manager: LinearLayoutManager) {
        binding.rvProductList.addOnScrollListener(object : PaginationScrollListener(manager) {
            override fun loadMoreItems() {
                isLoading = true
                binding.loading.visibility = View.VISIBLE
                viewModel.page++
                viewModel.getProducts()
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })

    }
}