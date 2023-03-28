package com.example.androidmvvmcleanarchitectureexample.ui.fragments.product

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.*
import com.example.androidmvvmcleanarchitectureexample.ui.fragments.dashboard.DashboardFragmentDirections
import com.example.common.adapters.genericadapter.GenericAdapter
import com.example.core.view.BaseMvvmFragment
import com.example.data.features.dashboard.models.*
import com.example.data.features.entry.model.CustomerResponseModel
import com.example.data.helper.manager.UserDataManager
import com.example.uitoolkit.custom.models.ItemModel
import com.example.uitoolkit.utils.ItemDecoration
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductFragment : BaseMvvmFragment<FragmentProductBinding, ProductViewModel>(
    R.layout.fragment_product,
    ProductViewModel::class
) {

    private var itemNo: String? = null

    private lateinit var productModel: ProductModel

    private val gson = Gson()

    private var itemDecorationCategoryRv = ItemDecoration(
        topSpace = 0,
        bottomSpace = 0,
        rightSpace = 0,
        leftSpace = 8
    )

    lateinit var customer: CustomerResponseModel


    private val mAdapter by lazy {
        GenericAdapter<ProductModel>(requireContext())
    }

    private val mSimilarGoodsAdapter by lazy {
        GenericAdapter<ProductModel>(requireContext())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemNo = requireArguments().getString("itemNo")
        productModel = gson.fromJson(
            requireArguments().getSerializable("product") as String,
            ProductModel::class.java
        )
        viewModel.getProduct(itemNo!!)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBuyNow.setOnClickListener { createOrder() }

        binding.btnAddToCart.setOnClickListener { addToCart() }

        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }


        binding.seeAll.setOnClickListener {  findNavController().navigate(
            ProductFragmentDirections.actionProductFragmentToProductsFragment(
                null
            )
        ) }

        viewModel.getProductResult().observe(viewLifecycleOwner) {
            val filteredList = it.subList(0,9)
            initImageRv(filteredList)
            binding.indicators.attachTo(binding.imageRv, true)
        }


        viewModel.getProductsResult().observe(viewLifecycleOwner) {
            initSimilarGoodsRv(it)
        }

        viewModel.getOrderResult().observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(),"Order created",Toast.LENGTH_SHORT).show()
        }

        viewModel.getCartResult().observe(viewLifecycleOwner) {
            Log.d("TAG", "onViewCreated: ")
            customer = it.customerId
        }

        viewModel.getCartListFailResult().observe(viewLifecycleOwner) {
            val request = CreateCart(product = productModel._id,cartQuantity = 1)
            viewModel.createCart(request)
        }

        viewModel.getAddToCartResult().observe(viewLifecycleOwner) {
            Log.d("TAG", "onViewCreatedCart: ")
            Toast.makeText(requireContext(),"Successful",Toast.LENGTH_SHORT).show()
        }

        binding.title.text = productModel.name
        binding.subTitle.text = productModel.description

        binding.productPrice.text = "US $" + productModel.currentPrice.toString()

        if(productModel.previousPrice != null) {
            binding.disCountText.visibility = View.VISIBLE
            binding.disCountText.text = "US $" +  productModel.previousPrice.toString()
        }else {
            binding.disCountText.visibility = View.GONE
        }

    }

    private fun createOrder() {
        val productList = ArrayList<OrderContent>()
        val orderContent = OrderContent(
            cartQuantity = 1,
            id = productModel._id,
            product = productModel
        )
        productList.add(orderContent)
        val createOrder = CreateOrder(
            letterSubject = "Thank you for order! You are welcome!",
            letterHtml = "Thank you for order! You are welcome!",
            shipping = "Kiev 50UAH",
            paymentInfo = "Credit card",
            status = "not shipped",
            email = customer.email,
            mobile = customer.telephone,
            products = productList
        )
        viewModel.createOrder(createOrder)
    }


    private fun addToCart() {
        val cartRequest = CartRequest(productId = productModel._id)
        viewModel.addToCart(cartRequest)
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
                val productModel = ItemModel(
                    percent = null,
                    imageurl = item.imageUrls!![0],
                    favouriteIconVisibility = true,
                    favouriteIconSelected = false,
                    previousPrice = item.previousPrice,
                    currentPrice = item.currentPrice
                )

                image.favouriteIconClick = {
                    viewModel.addProductToWishList(item._id!!)
                    productModel.favouriteIconSelected = true
                    image.setViewData(productModel)
                }
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

                    productPrice.text = "US $" + item.currentPrice.toString()
                    subTitle.text = item.description


                    productView.favouriteIconClick = {
                        viewModel.addProductToWishList(item._id!!)
                        productModel.favouriteIconSelected = true
                        productView.setViewData(productModel)
                    }

                    if(item.previousPrice != null) {
                        disCountText.visibility = View.VISIBLE
                        disCountText.text = "US $" +  item.previousPrice.toString()
                    }else {
                        disCountText.visibility = View.GONE
                    }
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