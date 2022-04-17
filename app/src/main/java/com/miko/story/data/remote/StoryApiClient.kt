package com.miko.story.data.remote

import com.miko.story.data.remote.response.BaseResponse
import com.miko.story.data.remote.response.LoginResponse
import com.miko.story.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface StoryApiClient {
    @POST("register")
    suspend fun register(@Body requestBody: RequestBody): Response<BaseResponse>

    @POST("login")
    suspend fun login(@Body requestBody: RequestBody): Response<LoginResponse>

    @GET("stories")
    suspend fun getAllStories(@Header("Authorization") token: String): Response<StoriesResponse>

    @POST("stories?size=0")
    @Multipart
    suspend fun addStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part,
    ): Response<BaseResponse>
}