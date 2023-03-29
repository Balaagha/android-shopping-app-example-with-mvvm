package com.example.androidmvvmcleanarchitectureexample.ui.fragments.wishList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentWishListBinding
import com.example.androidmvvmcleanarchitectureexample.databinding.ItemProductHorizontalBinding
import com.example.androidmvvmcleanarchitectureexample.databinding.ItemWishListBinding
import com.example.common.adapters.genericadapter.GenericAdapter
import com.example.core.view.BaseMvvmFragment
import com.example.data.features.dashboard.models.ProductModel
import com.example.uitoolkit.custom.models.ItemModel
import com.example.uitoolkit.utils.ItemDecoration
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishListFragment : BaseMvvmFragment<FragmentWishListBinding, WishListViewModel>(
    R.layout.fragment_wish_list,
    WishListViewModel::class
) {

    private val mAdapter by lazy {
        GenericAdapter<ProductModel>(requireContext())
    }


    private var itemDecoration = ItemDecoration(
        topSpace = 0,
        bottomSpace = 8,
        rightSpace = 16,
        leftSpace = 16
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        viewModel.getWishListResult().observe(viewLifecycleOwner) {
            initSimilarGoodsRv(it)
        }

    }


    private fun initSimilarGoodsRv(categoryList: List<ProductModel>) {
        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        with(binding.recyclerView) {
            layoutManager = manager
            adapter = mAdapter
        }
        binding.recyclerView.addItemDecoration(itemDecoration)

        mAdapter.setData(
            list = categoryList,
            notifyFunc = { mAdapter ->
                mAdapter.notifyDataSetChanged()
            }
        )

        mAdapter.expressionViewHolderBinding =
            { item, viewType, isAlreadyRendered, viewBinding ->
                val itemView = viewBinding as ItemWishListBinding
                with(itemView) {
                    val productModel = ItemModel(percent = null, imageurl = item.imageUrls!![0])
                    productView.setViewData(productModel)
                    title.text = item.name
                    subTitle.text = item.description
                    icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.favourite_icon_selected
                        )
                    )
                    icon.setOnClickListener {
                        viewModel.deleteProductFromWishList(item._id!!)
                        icon.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.favourite_icon_unselected
                            )
                        )

                    }

                    root.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putSerializable("product", Gson().toJson(item))
                        bundle.putString("itemNo", item.itemNo)
                        findNavController().navigate(
                            R.id.action_wishListFragment_to_productFragment,
                            bundle
                        )
                    }

                }


            }

        mAdapter.expressionOnCreateViewHolder = { viewGroup, viewType ->
            ItemWishListBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )

        }

    }
}