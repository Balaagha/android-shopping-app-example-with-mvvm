package com.example.androidmvvmcleanarchitectureexample.ui.fragments.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.*
import com.example.common.adapters.genericadapter.GenericAdapter
import com.example.core.view.BaseMvvmFragment
import com.example.data.features.dashboard.models.ProductModel
import com.example.uitoolkit.custom.models.ItemModel
import com.example.uitoolkit.utils.ItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : BaseMvvmFragment<FragmentProductBinding, ProductViewModel>(
    R.layout.fragment_product,
    ProductViewModel::class
) {
    private val args: ProductFragmentArgs by navArgs()

    private var itemNo: String? = null

    private var itemDecorationCategoryRv = ItemDecoration(
        topSpace = 0,
        bottomSpace = 0,
        rightSpace = 0,
        leftSpace = 8
    )


    private val mAdapter by lazy {
        GenericAdapter<ProductModel>(requireContext())
    }

    private val mSimilarGoodsAdapter by lazy {
        GenericAdapter<ProductModel>(requireContext())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemNo = args.itemNo
        viewModel.getProduct(itemNo!!)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //initSimilarGoodsRv(productList)

        viewModel.getProductResult().observe(viewLifecycleOwner) {
            val filteredList = it.subList(0,9)
            initImageRv(filteredList)
            binding.indicators.attachTo(binding.imageRv, true)
        }


        viewModel.getProductsResult().observe(viewLifecycleOwner) {
            initSimilarGoodsRv(it)
        }

    }


    private fun initImageRv(productList: List<ProductModel>) {
        val manager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
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
            with(itemView) {
                val productModel = ItemModel()
                if(item.imageUrls!!.isNotEmpty()) {
                    productModel.imageurl = item.imageUrls!![0]
                }
                image.setViewData(productModel)
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


    private fun initSimilarGoodsRv(categoryList: List<ProductModel>) {
        val manager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        with(binding.similarGoodsRv) {
            layoutManager = manager
            adapter = mSimilarGoodsAdapter
        }
        binding.similarGoodsRv.addItemDecoration(itemDecorationCategoryRv)

        mSimilarGoodsAdapter.setData(
            list = categoryList,
            notifyFunc = { mAdapter ->
                mAdapter.notifyDataSetChanged()
            }
        )

        mSimilarGoodsAdapter.expressionViewHolderBinding =
            { item, viewType, isAlreadyRendered, viewBinding ->
                val itemView = viewBinding as ItemProductHorizontalBinding
                with(itemView) {
                    val productModel = ItemModel(percent = null, imageurl = item.imageUrls!![0])
                    productView.setViewData(productModel)
                    root.setOnClickListener {

                    }
                }


            }

        mSimilarGoodsAdapter.expressionOnCreateViewHolder = { viewGroup, viewType ->
            ItemProductHorizontalBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )

        }

    }
}