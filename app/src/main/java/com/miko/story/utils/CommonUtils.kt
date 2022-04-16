package com.miko.story.utils

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.miko.story.R
import com.miko.story.data.util.Resource
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

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

fun Int.formatShorter(): String {
    val result: Float = this.toFloat() / 1000.toFloat()
    val decimalFormat = DecimalFormat("#.#").apply {
        roundingMode = RoundingMode.FLOOR
        decimalFormatSymbols = DecimalFormatSymbols(Locale.getDefault()).apply {
            decimalSeparator = '.'
        }
    }
    return when {
        result >= 1.0 -> {
            "${decimalFormat.format(result)}K"
        }
        else -> {
            "$this"
        }
    }
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

fun String.replaceWithDashIfEmpty(): String =
    this.ifEmpty { "-" }

fun Context.share(shareText: String) {
    val intent = ShareCompat.IntentBuilder(this)
        .setType("text/plain")
        .setText(shareText)
//        .setChooserTitle(getString(R.string.share_title_user))
        .createChooserIntent()
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
//        this.showToast(getString(R.string.error_share_app_not_found))
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PreferenceUtil.PREFERENCES_NAME)

fun AppCompatActivity.setupToolbar(title: String, showBack: Boolean){
    supportActionBar?.apply{
        this.title = title
        setDisplayHomeAsUpEnabled(showBack)
    }
}