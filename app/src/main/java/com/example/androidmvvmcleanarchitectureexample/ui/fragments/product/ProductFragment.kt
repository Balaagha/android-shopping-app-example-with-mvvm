package com.example.androidmvvmcleanarchitectureexample.ui.fragments.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.*
import com.example.androidmvvmcleanarchitectureexample.ui.fragments.dashboard.ProductViewModel
import com.example.common.adapters.genericadapter.GenericAdapter
import com.example.core.view.BaseMvvmFragment
import com.example.data.features.dashboard.models.ProductModel
import com.example.uitoolkit.utils.ItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : BaseMvvmFragment<FragmentProductBinding, ProductViewModel>(
    R.layout.fragment_product,
    ProductViewModel::class
) {

    private var itemDecorationCategoryRv = ItemDecoration(topSpace = 0,
        bottomSpace = 0,
        rightSpace = 0,
        leftSpace = 8)



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


       initProductRvVertical(productList)



    }


    private fun initProductRvVertical(productList: List<ProductModel>) {
        val manager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL,false)
        with(binding.imageRv) {
            layoutManager = manager
            adapter = mAdapter
        }

        binding.imageRv.addItemDecoration(itemDecorationCategoryRv)
        mAdapter.setData(
            list = productList,
            notifyFunc = { mAdapter ->
                mAdapter.notifyDataSetChanged()
            }
        )

        mAdapter.expressionViewHolderBinding = { item, viewType, isAlreadyRendered, viewBinding ->
            val itemView = viewBinding as ItemProductCardBinding
            with(itemView){
                //tvAdd.text = item.name
                root.setOnClickListener {

                }
            }


        }

        mAdapter.expressionOnCreateViewHolder = { viewGroup, viewType ->
            ItemProductCardBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )

        }

    }
}