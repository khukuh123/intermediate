package com.miko.story.domain.util

import androidx.recyclerview.widget.DiffUtil
import com.miko.story.domain.model.Story

object StoryDiffUtil {
    val diffUtil = object : DiffUtil.ItemCallback<Story>() {
        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean =
            oldItem == newItem
    }
}