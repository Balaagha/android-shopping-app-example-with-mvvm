package com.example.androidmvvmcleanarchitectureexample.ui.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

@AndroidEntryPoint
class DashboardFragment : BaseMvvmFragment<FragmentDashboardBinding, ProductViewModel>(
    R.layout.fragment_dashboard,
    ProductViewModel::class
) {

    private var itemDecorationCategoryRv = ItemDecoration(topSpace = 0,
        bottomSpace = 0,
        rightSpace = 0,
        leftSpace = 8)

    private var itemDecorationShopCategoryRv = ItemDecoration(topSpace = 0,
        bottomSpace = 0,
        rightSpace = 0,
        leftSpace = 8)

    private var itemDecorationProductRv = ItemDecoration(topSpace = 0,
        bottomSpace = 0,
        rightSpace = 0,
        leftSpace = 16)

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
        val product = ProductModel(name = "Nuraddin")
        val productList = ArrayList<ProductModel>()
        productList.add(product)


        val category = CategoryModel(name = "Nuraddin")
        val categoryList = ArrayList<CategoryModel>()
        categoryList.add(category)
        categoryList.add(category)
        categoryList.add(category)
        categoryList.add(category)
        categoryList.add(category)
        categoryList.add(category)


        initProductRv(productList)
        initCategoriesRv(categoryList)
        initShopCategoriesRv(categoryList)


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

        mProductAdapter.expressionViewHolderBinding = { item, viewType, isAlreadyRendered, viewBinding ->
            val itemView = viewBinding as ItemProductHorizontalBinding
            with(itemView){
                //tvAdd.text = item.name
                root.setOnClickListener {

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

        mCategoriesAdapter.expressionViewHolderBinding = { item, viewType, isAlreadyRendered, viewBinding ->
            val itemView = viewBinding as ItemCategoryBinding
            with(itemView){
                //tvAdd.text = item.name
                root.setOnClickListener {

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
        val manager = GridLayoutManager(requireContext(),4)
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

        mShopCategoriesAdapter.expressionViewHolderBinding = { item, viewType, isAlreadyRendered, viewBinding ->
            val itemView = viewBinding as ItemShopCategoryBinding
            with(itemView){
                //tvAdd.text = item.name
                root.setOnClickListener {

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