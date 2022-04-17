package com.miko.story.domain

import com.miko.story.domain.model.*
import com.miko.story.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface StoryUseCase {
    suspend fun register(registerParam: RegisterParam): Flow<Resource<Boolean>>
    suspend fun login(loginParam: LoginParam): Flow<Resource<User>>
    suspend fun getAllStories(token: String): Flow<Resource<List<Story>>>
    suspend fun addStory(addStoryParam: AddStoryParam): Flow<Resource<Boolean>>
}