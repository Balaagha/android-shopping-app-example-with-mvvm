package com.example.androidmvvmcleanarchitectureexample.ui.fragments.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.ItemProductHorizontalBinding
import com.example.data.features.dashboard.models.ProductModel

class SearchAdapter(val context: Context) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var dataList = mutableListOf<ProductModel>()

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


    }

    override fun getItemCount(): Int {

        return dataList.size
    }

    class ViewHolder(val binding: ItemProductHorizontalBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}