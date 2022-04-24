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
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @QueryMap query: Map<String, Int>,
    ): Response<StoriesResponse>

    @POST("stories")
    @Multipart
    @JvmSuppressWildcards
    suspend fun addStory(
        @Header("Authorization") token: String,
        @PartMap partMap: Map<String, RequestBody>,
        @Part image: MultipartBody.Part,
    ): Response<BaseResponse>
}