package com.miko.story.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


abstract class BasePagingViewHolder<T, VB : ViewBinding>(private val mBinding: VB) : RecyclerView.ViewHolder(mBinding.root) {
    val binding: VB
        get() = mBinding

    abstract fun bind(data: T?)
}