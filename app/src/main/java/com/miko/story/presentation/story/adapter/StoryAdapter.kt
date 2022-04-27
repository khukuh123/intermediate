package com.miko.story.presentation.story.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.miko.story.base.BasePagingAdapter
import com.miko.story.base.BasePagingViewHolder
import com.miko.story.databinding.ItemStoryBinding
import com.miko.story.domain.model.Story
import com.miko.story.domain.util.StoryDiffUtil
import com.miko.story.utils.setImageFromUrl

class StoryAdapter(
    private val onItemClicked: (Story, ImageView, TextView) -> Unit,
) : BasePagingAdapter<Story, ItemStoryBinding, StoryAdapter.ViewHolder>(StoryDiffUtil.diffUtil) {
    inner class ViewHolder(mBinding: ItemStoryBinding) : BasePagingViewHolder<Story, ItemStoryBinding>(mBinding) {
        override fun bind(data: Story?) {
            with(binding) {
                data?.let { data ->
                    imgStory.setImageFromUrl(data.photoUrl)
                    tvName.text = data.name
                    root.setOnClickListener {
                        onItemClicked.invoke(data, imgStory, tvName)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
}