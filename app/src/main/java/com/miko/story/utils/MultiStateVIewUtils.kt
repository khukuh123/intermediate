package com.miko.story.utils

import android.widget.ImageView
import android.widget.TextView
import com.kennyc.view.MultiStateView
import com.miko.story.R

fun MultiStateView.showEmptyList(title: String? = null, message: String? = null, imageResId: Int? = null) {
    this.viewState = MultiStateView.ViewState.EMPTY
    this.getView(MultiStateView.ViewState.EMPTY)?.let {
        val imgEmpty: ImageView = it.findViewById(R.id.imgEmpty)
        val tvEmptyTitle: TextView = it.findViewById(R.id.tvEmptyTitle)
        val tvEmptyMessage: TextView = it.findViewById(R.id.tvEmptyMessage)

        if (imageResId != null) {
            imgEmpty.setImageResource(imageResId)
        } else if (imgEmpty.drawable == null) {
            imgEmpty.gone()
        }
        title?.let { text -> tvEmptyTitle.text = text }
        message?.let { text ->
            if (text.isEmpty()) tvEmptyMessage.gone() else tvEmptyMessage.text = text
        }
    }
}