package com.miko.story.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.miko.story.R
import com.miko.story.domain.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.gone() {
    visibility = View.GONE
}

@SuppressLint("CheckResult")
fun ImageView.setImageFromUrl(image: String, size: Int? = null) {
    val request = RequestOptions().apply {
        error(R.drawable.ic_baseline_image_24)
        placeholder(R.drawable.ic_baseline_image_24)
    }
    if (size != null) request.override(size)
    Glide.with(this).load(image).apply(request).into(this)
}

fun <T> LiveData<Resource<T>>.observe(
    lifecycleOwner: LifecycleOwner,
    onLoading: () -> Unit,
    onSuccess: (items: T) -> Unit,
    onError: (errorMessage: String) -> Unit,
) {
    observe(lifecycleOwner) {
        when (it) {
            is Resource.Loading<T> -> onLoading.invoke()
            is Resource.Success<T> -> it.data?.let { data -> onSuccess.invoke(data) }
            else -> onError.invoke(it.errorMessage.orEmpty())
        }
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PreferenceUtil.PREFERENCES_NAME)

fun AppCompatActivity.setupToolbar(title: String, showBack: Boolean) {
    supportActionBar?.apply {
        this.title = title
        setDisplayHomeAsUpEnabled(showBack)
    }
}

fun <T> CoroutineScope.collectResult(liveData: MutableLiveData<T>, block: suspend () -> Flow<T>) {
    this.launch {
        val result = block.invoke()
        result.collect {
            liveData.postValue(it)
        }
    }
}