package com.miko.story.domain.util

import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.miko.story.data.remote.response.BaseResponse
import com.miko.story.data.remote.response.LoginResult
import com.miko.story.data.remote.response.StoryItem
import com.miko.story.domain.model.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

private val gson = Gson()

fun BaseResponse.map(): Boolean =
    this.error ?: false

fun AddStoryParam.map(): Map<String, RequestBody> =
    mutableMapOf<String, RequestBody>().apply {
        this["description"] = this@map.description.toRequestBody("text/plain".toMediaType())
        if (location != null) {
            this["lat"] = this@map.location.latitude.toString().toRequestBody("text/plain".toMediaType())
            this["lon"] = this@map.location.longitude.toString().toRequestBody("text/plain".toMediaType())
        }
    }

fun StoriesParam.map(): Map<String, Int> =
    mutableMapOf<String, Int>().apply {
        if (this@map.location != null) this["location"] = if (this@map.location) 1 else 0
        this["size"] = this@map.size
        this["page"] = this@map.page
    }

fun StoryItem.map() =
    Story(
        id = id ?: "",
        photoUrl = photoUrl ?: "",
        name = name ?: "",
        description = description ?: "",
        latLng = LatLng(lat ?: 0.00, lon ?: 0.00)
    )

fun LoginResult.map() =
    User(
        userId = userId ?: "",
        name = name ?: "",
        token = token ?: ""
    )

fun RegisterParam.map(): RequestBody =
    gson.toJson(this).toRequestBody("application/json".toMediaType())

fun LoginParam.map(): RequestBody =
    gson.toJson(this).toRequestBody("application/json".toMediaType())