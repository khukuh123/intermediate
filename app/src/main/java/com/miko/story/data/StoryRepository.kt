package com.miko.story.data

import com.miko.story.data.remote.response.LoginResponse
import com.miko.story.data.remote.response.RegisterResponse
import com.miko.story.data.util.ApiResult
import com.miko.story.domain.model.LoginParam
import com.miko.story.domain.model.RegisterParam
import kotlinx.coroutines.flow.Flow

interface StoryRepository {
    suspend fun register(registerParam: RegisterParam): Flow<ApiResult<RegisterResponse>>
    suspend fun login(loginParam: LoginParam): Flow<ApiResult<LoginResponse>>
}