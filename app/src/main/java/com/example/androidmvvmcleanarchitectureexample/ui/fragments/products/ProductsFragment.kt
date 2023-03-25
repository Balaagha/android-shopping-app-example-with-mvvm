package com.example.androidmvvmcleanarchitectureexample.ui.fragments.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentProductsBinding
import com.example.androidmvvmcleanarchitectureexample.databinding.ItemProductHorizontalBinding
import com.example.androidmvvmcleanarchitectureexample.databinding.ItemProductVerticalBinding
import com.example.androidmvvmcleanarchitectureexample.ui.fragments.dashboard.ProductViewModel
import com.example.common.adapters.genericadapter.GenericAdapter
import com.example.core.view.BaseMvvmFragment
import com.example.data.features.dashboard.models.ProductModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : BaseMvvmFragment<FragmentProductsBinding, ProductViewModel>(
    R.layout.fragment_products,
    ProductViewModel::class
) {


    private val mAdapter by lazy {
        GenericAdapter<ProductModel>(requireContext())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = ProductModel(name = "Nuraddin")
        val productList = ArrayList<ProductModel>()
        productList.add(product)
        productList.add(product)
        productList.add(product)
        productList.add(product)
        productList.add(product)
        productList.add(product)
        productList.add(product)
        productList.add(product)
        productList.add(product)


        initProductRvGrid(productList)

        binding.decoration.setOnClickListener {
            initProductRvVertical(productList)
        }


    }


    private fun initProductRvVertical(productList: List<ProductModel>) {
        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        with(binding.recyclerView) {
            layoutManager = manager
            adapter = mAdapter
        }


        mAdapter.setData(
            list = productList,
            notifyFunc = { mAdapter ->
                mAdapter.notifyDataSetChanged()
            }
        )

        mAdapter.expressionViewHolderBinding = { item, viewType, isAlreadyRendered, viewBinding ->
            val itemView = viewBinding as ItemProductVerticalBinding
            with(itemView){
                //tvAdd.text = item.name
                root.setOnClickListener {

                }
            }


        }

        mAdapter.expressionOnCreateViewHolder = { viewGroup, viewType ->
            ItemProductVerticalBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )

        }

    }

    private fun initProductRvGrid(productList: List<ProductModel>) {
        val manager = GridLayoutManager(requireContext(), 2)
        with(binding.recyclerView) {
            layoutManager = manager
            adapter = mAdapter
        }


        mAdapter.setData(
            list = productList,
            notifyFunc = { mAdapter ->
                mAdapter.notifyDataSetChanged()
            }
        )

        mAdapter.expressionViewHolderBinding = { item, viewType, isAlreadyRendered, viewBinding ->
            val itemView = viewBinding as ItemProductHorizontalBinding
            with(itemView){
                //tvAdd.text = item.name
                root.setOnClickListener {

                }
            }


        }

        mAdapter.expressionOnCreateViewHolder = { viewGroup, viewType ->
            ItemProductHorizontalBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )

        }

    }
}