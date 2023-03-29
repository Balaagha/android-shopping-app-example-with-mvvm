package com.example.androidmvvmcleanarchitectureexample.ui.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentSearchBinding
import com.example.androidmvvmcleanarchitectureexample.databinding.ItemProductHorizontalBinding
import com.example.androidmvvmcleanarchitectureexample.ui.fragments.products.ProductsFragmentDirections
import com.example.common.adapters.genericadapter.GenericAdapter
import com.example.core.view.BaseMvvmFragment
import com.example.data.features.dashboard.models.ProductModel
import com.example.uitoolkit.custom.models.ItemModel
import com.example.uitoolkit.utils.DelayedOnQueryTextListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : BaseMvvmFragment<FragmentSearchBinding, SearchViewModel>(
    R.layout.fragment_search,
    SearchViewModel::class
) {

    private lateinit var mAdapter :SearchAdapter

    private lateinit var gridLayoutManager: GridLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initProductRvGrid()

        viewModel.getSearchResult().observe(viewLifecycleOwner) {
           mAdapter.addData(it as MutableList<ProductModel>)
        }

        binding.search.setOnQueryTextListener(object : DelayedOnQueryTextListener() {

            override fun onDelayerQueryTextChange(query: String?) {
                if(query!!.isNotEmpty()) {
                    viewModel.searchProducts(query.toString())
                }
            }
        })
    }


    private fun initProductRvGrid() {
        gridLayoutManager = GridLayoutManager(requireContext(), 2)
        mAdapter = SearchAdapter(requireContext())
        mAdapter.favouriteIconClick = {
            viewModel.addProductToWishList(it)
        }
        binding.recyclerView.layoutManager = gridLayoutManager
        binding.recyclerView.adapter = mAdapter
    }
}