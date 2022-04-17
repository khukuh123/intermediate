package com.miko.story.domain.util

import com.google.gson.Gson
import com.miko.story.data.remote.response.LoginResponse
import com.miko.story.data.remote.response.LoginResult
import com.miko.story.domain.model.LoginParam
import com.miko.story.domain.model.RegisterParam
import com.miko.story.domain.model.User
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object StoryMapper {

    private val gson = Gson()

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
}