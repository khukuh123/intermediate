package com.miko.story.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miko.story.databinding.ItemLoadingBinding

class StoryLoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<StoryLoadingStateAdapter.StoryLoadingStateViewHolder>() {
    override fun onBindViewHolder(holder: StoryLoadingStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): StoryLoadingStateViewHolder {
        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return StoryLoadingStateViewHolder(binding, retry)
    }

    class StoryLoadingStateViewHolder(private val binding: ItemLoadingBinding, retry: () -> Unit) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            with(binding) {
                if (loadState is LoadState.Error) {
                    tvError.text = loadState.error.message
                }
                pbLoading.isVisible = loadState is LoadState.Loading
                tvError.isVisible = loadState is LoadState.Error
                tvError.isVisible = loadState is LoadState.Error
            }
        }
    }
}