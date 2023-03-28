package com.example.androidmvvmcleanarchitectureexample.ui.fragments.products

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentProductsBinding
import com.example.androidmvvmcleanarchitectureexample.databinding.ItemProductHorizontalBinding
import com.example.androidmvvmcleanarchitectureexample.databinding.ItemProductVerticalBinding
import com.example.common.adapters.genericadapter.GenericAdapter
import com.example.common.adapters.genericadapter.PaginationScrollListener
import com.example.core.view.BaseMvvmFragment
import com.example.data.features.dashboard.models.ProductModel
import com.example.uitoolkit.custom.models.ItemModel
import com.example.uitoolkit.utils.ItemDecoration
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : BaseMvvmFragment<FragmentProductsBinding, ProductsViewModel>(
    R.layout.fragment_products,
    ProductsViewModel::class
) {

    private val args: ProductsFragmentArgs by navArgs()

    private var id: String? = null

    private var listItemsIsVertical = false

    private var productList = ArrayList<ProductModel>()

    private var isLoading = false
    private var isLastPage = false

    private val mAdapter by lazy {
        GenericAdapter<ProductModel>(requireContext())
    }

    private lateinit var manager: LinearLayoutManager

    private lateinit var gridLayoutManager: GridLayoutManager

    private var itemDecoration = ItemDecoration(
        topSpace = 0,
        bottomSpace = 8,
        rightSpace = 0,
        leftSpace = 0
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        gridLayoutManager = GridLayoutManager(requireContext(), 2)
        id = args.id
        viewModel.getProducts(id)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProductRvGrid()
        setUpScrollListener(gridLayoutManager)


        binding.decoration.setOnClickListener {
            if (!listItemsIsVertical) {
                listItemsIsVertical = true
                initProductRvVertical()
                setUpScrollListener(manager)
            } else {
                listItemsIsVertical = false
                initProductRvGrid()
                setUpScrollListener(gridLayoutManager)
            }

        }

        binding.search.setOnClickListener {
            findNavController().navigate(R.id.action_productsFragment_to_searchFragment)
        }

        viewModel.getProductsResult().observe(viewLifecycleOwner) {
            if (it.size < 20) {
                isLastPage = true
            }
            isLoading = false
            binding.loading.visibility = View.GONE
            productList.addAll(it)
            mAdapter.setData(
                list = productList,
                notifyFunc = { mAdapter ->
                    mAdapter.notifyDataSetChanged()
                }
            )
        }
    }

    private fun setUpScrollListener(manager: LinearLayoutManager) {
        binding.recyclerView.addOnScrollListener(object : PaginationScrollListener(manager) {
            override fun loadMoreItems() {
                isLoading = true
                binding.loading.visibility = View.VISIBLE
                viewModel.page++
                viewModel.getProducts(id)
            }

            override fun isLastPage(): Boolean {

                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })

}


private fun initProductRvVertical() {
    with(binding.recyclerView) {
        layoutManager = manager
        adapter = mAdapter
    }

    binding.recyclerView.addItemDecoration(itemDecoration)

    mAdapter.expressionViewHolderBinding = { item, viewType, isAlreadyRendered, viewBinding ->
        val itemView = viewBinding as ItemProductVerticalBinding
        with(itemView) {
            val productModel = ItemModel(
                percent = null,
                imageurl = item.imageUrls!![0],
                favouriteIconVisibility = false,
                favouriteIconSelected = false,
                previousPrice = item.previousPrice,
                currentPrice = item.currentPrice
            )
            productView.setViewData(productModel)
            productView.getPercentText().setTextSize(TypedValue.COMPLEX_UNIT_SP,8F)
            title.text = item.name

            productPrice.text = "US $" + item.currentPrice.toString()
            subTitle.text = item.description

            if(item.previousPrice != null) {
                disCountText.visibility = View.VISIBLE
                disCountText.text = "US $" +  item.previousPrice.toString()
            }else {
                disCountText.visibility = View.GONE
            }
            //tvAdd.text = item.name
            root.setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable("product", Gson().toJson(item))
                bundle.putString("itemNo",item.itemNo)
                findNavController().navigate(R.id.action_productsFragment_to_productFragment,bundle)
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

private fun initProductRvGrid() {
    with(binding.recyclerView) {
        layoutManager = gridLayoutManager
        adapter = mAdapter
    }

    binding.recyclerView.addItemDecoration(itemDecoration)

    mAdapter.expressionViewHolderBinding = { item, viewType, isAlreadyRendered, viewBinding ->
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

            if(item.previousPrice != null) {
                disCountText.visibility = View.VISIBLE
                disCountText.text = "US $" +  item.previousPrice.toString()
            }else {
                disCountText.visibility = View.GONE
            }
            //tvAdd.text = item.name
            root.setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable("product", Gson().toJson(item))
                bundle.putString("itemNo",item.itemNo)
                findNavController().navigate(R.id.action_productsFragment_to_productFragment,bundle)
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