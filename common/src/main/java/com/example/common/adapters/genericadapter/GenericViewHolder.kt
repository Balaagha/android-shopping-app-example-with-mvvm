package com.example.common.adapters.genericadapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


class GenericViewHolder<T> internal constructor(
    private val binding: ViewBinding,
    private val experssion: (model: T, viewType: Int, isAlreadyRendered: Boolean, mBinding: ViewBinding) -> Unit,
    private val viewType: Int
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: T, isNotRenderedBefore: Boolean): ViewBinding {
        experssion(item, viewType, isNotRenderedBefore, binding)
        return binding
    }
}