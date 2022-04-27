package com.miko.story.base

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding

abstract class BasePagingAdapter<T : Any, VB : ViewBinding, VH : BasePagingViewHolder<T, VB>>(
    diffUtil: DiffUtil.ItemCallback<T>,
) : PagingDataAdapter<T, VH>(
    diffUtil
) {
    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}