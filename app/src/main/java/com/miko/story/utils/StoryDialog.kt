package com.miko.story.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.miko.story.R
import com.miko.story.databinding.LayoutLoadingBinding

fun getLoadingDialog(context: Context) =
    AlertDialog.Builder(context, R.style.StoryLoadingDialog).apply {
        val binding = LayoutLoadingBinding.inflate(LayoutInflater.from(context))
        setView(binding.root)
        setCancelable(false)
    }.create()

fun getErrorDialog(context: Context) =
    AlertDialog.Builder(context).apply {
        setCancelable(true)
    }.create()