package com.miko.story.data

import com.miko.story.data.remote.response.BaseResponse
import com.miko.story.data.remote.response.LoginResponse
import com.miko.story.data.remote.response.StoriesResponse
import com.miko.story.data.util.ApiResult
import com.miko.story.domain.model.AddStoryParam
import com.miko.story.domain.model.LoginParam
import com.miko.story.domain.model.RegisterParam
import com.miko.story.domain.model.StoriesParam
import kotlinx.coroutines.flow.Flow

interface StoryRepository {
    suspend fun register(registerParam: RegisterParam): Flow<ApiResult<BaseResponse>>
    suspend fun login(loginParam: LoginParam): Flow<ApiResult<LoginResponse>>
    suspend fun getAllStories(token: String, storiesParam: StoriesParam): Flow<ApiResult<StoriesResponse>>
    suspend fun addStory(addStoryParam: AddStoryParam): Flow<ApiResult<BaseResponse>>
}