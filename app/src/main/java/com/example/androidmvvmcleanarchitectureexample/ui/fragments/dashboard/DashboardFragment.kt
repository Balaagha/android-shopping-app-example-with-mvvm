package com.example.androidmvvmcleanarchitectureexample.ui.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentDashboardBinding
import com.example.androidmvvmcleanarchitectureexample.databinding.ItemProductHorizontalBinding
import com.example.common.adapters.genericadapter.GenericAdapter
import com.example.core.view.BaseMvvmFragment
import com.example.data.features.dashboard.models.Product
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseMvvmFragment<FragmentDashboardBinding, DashboardViewModel>(
    R.layout.fragment_dashboard,
    DashboardViewModel::class
) {

    private val mCategoryAdapter by lazy {
        GenericAdapter<Product>(requireContext())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = Product(name = "Nuraddin")
        val productList = ArrayList<Product>()
        productList.add(product)

        val manager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        with(binding.recyclerView) {
            layoutManager = manager
            adapter = mCategoryAdapter
        }

        mCategoryAdapter.setData(
            list = productList,
            notifyFunc = { mAdapter ->
                mAdapter.notifyDataSetChanged()
            }
        )

        mCategoryAdapter.expressionViewHolderBinding = { item, viewType, isAlreadyRendered, viewBinding ->
                    val itemView = viewBinding as ItemProductHorizontalBinding
                    with(itemView){
                        //tvAdd.text = item.name
                        root.setOnClickListener {

                        }
                    }


        }

        mCategoryAdapter.expressionOnCreateViewHolder = { viewGroup, viewType ->
            ItemProductHorizontalBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )

            }

    }


}