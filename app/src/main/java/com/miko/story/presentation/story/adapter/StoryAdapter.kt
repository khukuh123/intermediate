package com.miko.story.presentation.story.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.miko.story.base.BaseAdapter
import com.miko.story.base.BaseViewHolder
import com.miko.story.databinding.ItemStoryBinding
import com.miko.story.domain.model.Story
import com.miko.story.domain.util.StoryDiffUtil
import com.miko.story.utils.setImageFromUrl

class StoryAdapter(
    private val onItemClicked: (Story) -> Unit
): BaseAdapter<Story, ItemStoryBinding, StoryAdapter.ViewHolder>(StoryDiffUtil.diffUtil) {
    inner class ViewHolder(mBinding: ItemStoryBinding) : BaseViewHolder<Story, ItemStoryBinding>(mBinding){
        override fun bind(data: Story) {
            with(binding){
                imgStory.setImageFromUrl(data.storyUrl)
                tvName.text = data.name
                root.setOnClickListener {
                    onItemClicked.invoke(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
}