package com.example.common.adapters.genericadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.common.utils.extentions.isNotNull


class GenericAdapter<T>(var context: Context? = null) :
    RecyclerView.Adapter<GenericViewHolder<T>>() {

    var lastPosition = DEFAULT_LAST_ITEM_POSITION

    val lastElement: T?
        get() = run {
            if (lastPosition >= 0) {
                listOfItems[lastPosition]
            } else {
                null
            }
        }

    private var listOfItems: List<T> = listOf()

    fun removeLastItem() {
        if (listOfItems.isNotEmpty()) {
            val removedElementPosition = listOfItems.size - 1
            listOfItems = listOfItems.subList(0, removedElementPosition)
            notifyItemRemoved(removedElementPosition)
            lastPosition = listOfItems.size - 1
        }
    }

    fun setData(list: List<T>?, notifyFunc: (mAdapter: GenericAdapter<T>) -> Unit) {
        list?.let {
            listOfItems = it
            notifyFunc.invoke(this)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData(){
        lastPosition = DEFAULT_LAST_ITEM_POSITION
        listOfItems = listOf()
        notifyDataSetChanged()
    }

    var expressionViewHolderBinding: ((model: T, viewType: Int, isAlreadyRendered: Boolean, mBinding: ViewBinding) -> Unit)? =
        null
    var expressionOnCreateViewHolder: ((ViewGroup, Int) -> ViewBinding)? = null

    var expressionOnGetItemViewType: ((itemList: List<T>, Int) -> Int)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<T> {
        return expressionOnCreateViewHolder?.let {
            it(parent, viewType)
        }?.let {
            GenericViewHolder(it, expressionViewHolderBinding!!, viewType)
        }!!
    }

    override fun onBindViewHolder(holder: GenericViewHolder<T>, position: Int) {
        val isNotRenderedBefore = position > lastPosition
        holder.bind(listOfItems[position], isNotRenderedBefore)
        if (isNotRenderedBefore) {
            lastPosition = position
        }
    }


    override fun getItemViewType(position: Int): Int =
        if (expressionOnGetItemViewType?.isNotNull() == true && listOfItems.isNotEmpty()) {
            expressionOnGetItemViewType?.invoke(listOfItems, position)
                ?: DEFAULT_ITEM_COUNT
        } else {
            DEFAULT_VIEW_TYPE
        }

    override fun getItemCount(): Int {
        return listOfItems.size
    }

    companion object {
        const val DEFAULT_VIEW_TYPE = 0
        const val DEFAULT_ITEM_COUNT = 0
        const val DEFAULT_LAST_ITEM_POSITION = -1
    }

}