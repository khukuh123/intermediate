package com.miko.story.utils

import android.widget.ImageView
import android.widget.TextView
import com.kennyc.view.MultiStateView
import com.miko.story.R

fun MultiStateView.showError(title: String? = null, message: String? = null, imageResId: Int? = null) {
    this.viewState = MultiStateView.ViewState.ERROR
    this.getView(MultiStateView.ViewState.ERROR)?.let {
//        val imgError: ImageView = it.findViewById(R.id.imgError)
//        val tvErrorTitle: TextView = it.findViewById(R.id.tvErrorTitle)
//        val tvErrorMessage: TextView = it.findViewById(R.id.tvErrorMessage)

//        if (imageResId != null) {
//            imgError.setImageResource(imageResId)
//        } else if (imgError.drawable == null) {
//            imgError.gone()
//        }
//        title?.let { text -> tvErrorTitle.text = text }
//        message?.let { text -> tvErrorMessage.text = text }
    }
}

fun MultiStateView.showLoading() {
    this.viewState = MultiStateView.ViewState.LOADING
}

fun MultiStateView.showEmptyList(title: String? = null, message: String? = null, imageResId: Int? = null) {
    this.viewState = MultiStateView.ViewState.EMPTY
    this.getView(MultiStateView.ViewState.EMPTY)?.let {
//        val imgEmpty: ImageView = it.findViewById(R.id.imgEmpty)
//        val tvEmptyTitle: TextView = it.findViewById(R.id.tvEmptyTitle)
//        val tvEmptyMessage: TextView = it.findViewById(R.id.tvEmptyMessage)

//        if (imageResId != null) {
//            imgEmpty.setImageResource(imageResId)
//        } else if (imgEmpty.drawable == null) {
//            imgEmpty.gone()
//        }
//        title?.let { text -> tvEmptyTitle.text = text }
//        message?.let { text ->
//            if (text.isEmpty()) tvEmptyMessage.gone() else tvEmptyMessage.text = text
//        }
    }
}

fun MultiStateView.showContent() {
    this.viewState = MultiStateView.ViewState.CONTENT
}