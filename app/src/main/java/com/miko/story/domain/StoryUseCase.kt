package com.miko.story.domain

import com.miko.story.domain.model.LoginParam
import com.miko.story.domain.model.RegisterParam
import com.miko.story.domain.model.User
import com.miko.story.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface StoryUseCase {
    suspend fun register(registerParam: RegisterParam): Flow<Resource<Boolean>>
    suspend fun login(loginParam: LoginParam): Flow<Resource<User>>
}