package com.example.androidmvvmcleanarchitectureexample.ui.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentDashboardBinding
import com.example.androidmvvmcleanarchitectureexample.databinding.ItemCategoryBinding
import com.example.androidmvvmcleanarchitectureexample.databinding.ItemProductHorizontalBinding
import com.example.androidmvvmcleanarchitectureexample.databinding.ItemShopCategoryBinding
import com.example.common.adapters.genericadapter.GenericAdapter
import com.example.core.view.BaseMvvmFragment
import com.example.data.features.dashboard.models.CategoryModel
import com.example.data.features.dashboard.models.ProductModel
import com.example.uitoolkit.utils.ItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import com.example.uitoolkit.custom.models.ItemModel
import com.google.gson.Gson

@AndroidEntryPoint
class DashboardFragment : BaseMvvmFragment<FragmentDashboardBinding, DashboardViewModel>(
    R.layout.fragment_dashboard,
    DashboardViewModel::class
) {

    private var itemDecorationCategoryRv = ItemDecoration(
        topSpace = 0,
        bottomSpace = 0,
        rightSpace = 0,
        leftSpace = 8
    )

    private var itemDecorationShopCategoryRv = ItemDecoration(
        topSpace = 0,
        bottomSpace = 10,
        rightSpace = 0,
        leftSpace = 8
    )

    private var itemDecorationProductRv = ItemDecoration(
        topSpace = 0,
        bottomSpace = 0,
        rightSpace = 0,
        leftSpace = 16
    )

    private val mCategoriesAdapter by lazy {
        GenericAdapter<CategoryModel>(requireContext())
    }

    private val mShopCategoriesAdapter by lazy {
        GenericAdapter<CategoryModel>(requireContext())
    }

    private val mProductAdapter by lazy {
        GenericAdapter<ProductModel>(requireContext())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCategoriesResult().observe(viewLifecycleOwner) {
            initCategoriesRv(it)
            initShopCategoriesRv(it)
        }

        viewModel.getProductsResult().observe(viewLifecycleOwner) {
            initProductRv(it)
        }


        binding.favouriteIcon.setOnClickListener { findNavController().navigate(R.id.action_dashboardFragment_to_wishListFragment) }

        binding.seeAll.setOnClickListener {
            findNavController().navigate(
                DashboardFragmentDirections.actionDashboardFragmentToProductsFragment(
                    null
                )
            )
        }
        binding.search.setOnClickListener { findNavController().navigate(R.id.action_dashboardFragment_to_searchFragment) }

    }


    private fun initProductRv(productList: List<ProductModel>) {
        val manager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        with(binding.recyclerView) {
            layoutManager = manager
            adapter = mProductAdapter
        }

        binding.recyclerView.addItemDecoration(itemDecorationProductRv)


        mProductAdapter.setData(
            list = productList,
            notifyFunc = { mAdapter ->
                mAdapter.notifyDataSetChanged()
            }
        )

        mProductAdapter.expressionViewHolderBinding =
            { item, viewType, isAlreadyRendered, viewBinding ->
                val itemView = viewBinding as ItemProductHorizontalBinding
                with(itemView) {
                    val productModel = ItemModel(
                        percent = null,
                        imageurl = item.imageUrls!![0],
                        favouriteIconVisibility = true,
                        favouriteIconSelected = false,
                        previousPrice = item.previousPrice,
                        currentPrice = item.currentPrice
                    )
                    productView.setViewData(productModel)
                    title.text = item.name
                    productView.favouriteIconClick = {
                        viewModel.addProductToWishList(item._id!!)
                        productModel.favouriteIconSelected = true
                        productView.setViewData(productModel)
                    }

                    productPrice.text = "US $" + item.currentPrice.toString()
                    subTitle.text = item.description

                    if (item.previousPrice != null) {
                        disCountText.visibility = View.VISIBLE
                        disCountText.text = "US $" + item.previousPrice.toString()
                    } else {
                        disCountText.visibility = View.GONE
                    }
                    //tvAdd.text = item.name
                    root.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putSerializable("product", Gson().toJson(item))
                        bundle.putString("itemNo", item.itemNo)
                        findNavController().navigate(
                            R.id.action_dashboardFragment_to_productFragment,
                            bundle
                        )
                    }
                }


            }

        mProductAdapter.expressionOnCreateViewHolder = { viewGroup, viewType ->
            ItemProductHorizontalBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )

        }

    }

    private fun initCategoriesRv(categoryList: List<CategoryModel>) {
        val manager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        with(binding.categoriesRv) {
            layoutManager = manager
            adapter = mCategoriesAdapter
        }
        binding.categoriesRv.addItemDecoration(itemDecorationCategoryRv)

        mCategoriesAdapter.setData(
            list = categoryList,
            notifyFunc = { mAdapter ->
                mAdapter.notifyDataSetChanged()
            }
        )

        mCategoriesAdapter.expressionViewHolderBinding =
            { item, viewType, isAlreadyRendered, viewBinding ->
                val itemView = viewBinding as ItemCategoryBinding
                with(itemView) {
                    title.text = item.name
                    root.setOnClickListener {
                        findNavController().navigate(
                            DashboardFragmentDirections.actionDashboardFragmentToProductsFragment(
                                item.name!!
                            )
                        )
                    }
                }


            }

        mCategoriesAdapter.expressionOnCreateViewHolder = { viewGroup, viewType ->
            ItemCategoryBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )

        }

    }

    private fun initShopCategoriesRv(categoryList: List<CategoryModel>) {
        val manager = GridLayoutManager(requireContext(), 4)
        with(binding.shopCategoryRv) {
            layoutManager = manager
            adapter = mShopCategoriesAdapter
        }

        binding.shopCategoryRv.addItemDecoration(itemDecorationShopCategoryRv)

        mShopCategoriesAdapter.setData(
            list = categoryList,
            notifyFunc = { mAdapter ->
                mAdapter.notifyDataSetChanged()
            }
        )

        mShopCategoriesAdapter.expressionViewHolderBinding =
            { item, viewType, isAlreadyRendered, viewBinding ->
                val itemView = viewBinding as ItemShopCategoryBinding
                with(itemView) {
                    name.text = item.name
                    Glide.with(requireActivity())
                        .load(item.imgUrl)
                        .into(image)
                    root.setOnClickListener {
                        findNavController().navigate(
                            DashboardFragmentDirections.actionDashboardFragmentToProductsFragment(
                                item.name!!
                            )
                        )

                    }
                }


            }

        mShopCategoriesAdapter.expressionOnCreateViewHolder = { viewGroup, viewType ->
            ItemShopCategoryBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )

        }
    }


}