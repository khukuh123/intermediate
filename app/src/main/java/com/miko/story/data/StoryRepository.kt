package com.miko.story.data

import com.miko.story.data.remote.response.BaseResponse
import com.miko.story.data.remote.response.LoginResponse
import com.miko.story.data.remote.response.StoriesResponse
import com.miko.story.data.util.ApiResult
import com.miko.story.domain.model.*
import com.miko.story.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface StoryRepository {
    suspend fun register(registerParam: RegisterParam): Flow<Resource<Boolean>>
    suspend fun login(loginParam: LoginParam): Flow<Resource<User>>
    suspend fun getAllStories(token: String, storiesParam: StoriesParam): Flow<Resource<List<Story>>>
    suspend fun addStory(addStoryParam: AddStoryParam): Flow<Resource<Boolean>>
}