package com.miko.story.data.remote

import com.miko.story.data.remote.response.LoginResponse
import com.miko.story.data.remote.response.RegisterResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface StoryApiClient {
    @POST("register")
    suspend fun register(@Body requestBody: RequestBody): Response<RegisterResponse>

    @POST("login")
    suspend fun login(@Body requestBody: RequestBody): Response<LoginResponse>
}