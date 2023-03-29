package com.example.androidmvvmcleanarchitectureexample.ui.fragments.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.ItemProductHorizontalBinding
import com.example.data.features.dashboard.models.ProductModel
import com.example.uitoolkit.custom.models.ItemModel

class SearchAdapter(val context: Context) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var dataList = mutableListOf<ProductModel>()

    var favouriteIconClick: ((String) -> Unit)? = null

    fun addData(dataList: MutableList<ProductModel>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val binding: ItemProductHorizontalBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_product_horizontal,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        val item = dataList[position]
        val productModel = ItemModel(
            percent = null,
            favouriteIconVisibility = true,
            favouriteIconSelected = false,
            previousPrice = item.previousPrice,
            currentPrice = item.currentPrice
        )
        if(item.imageUrls!!.isNotEmpty()) {
            productModel.imageurl = item.imageUrls!![0]
        }
        holder.binding.productView.setViewData(productModel)
        holder.binding.title.text = item.name
        holder.binding.productView.favouriteIconClick = {
            favouriteIconClick?.invoke(item._id!!)
            productModel.favouriteIconSelected = true
            holder.binding.productView.setViewData(productModel)
        }

        holder.binding.productPrice.text = "US $" + item.currentPrice.toString()
        holder.binding.subTitle.text = item.description

        if(item.previousPrice != null) {
            holder.binding.disCountText.visibility = View.VISIBLE
            holder.binding.disCountText.text = "US $" +  item.previousPrice.toString()
        }else {
            holder.binding.disCountText.visibility = View.GONE
        }


    }

    override fun getItemCount(): Int {

        return dataList.size
    }

    class ViewHolder(val binding: ItemProductHorizontalBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}