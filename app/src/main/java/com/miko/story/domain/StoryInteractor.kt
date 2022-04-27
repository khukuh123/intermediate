package com.miko.story.domain

import com.miko.story.data.StoryRepository
import com.miko.story.domain.model.*
import com.miko.story.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class StoryInteractor(private val repository: StoryRepository) : StoryUseCase {
    override suspend fun register(registerParam: RegisterParam): Flow<Resource<Boolean>> =
        repository.register(registerParam)

    override suspend fun login(loginParam: LoginParam): Flow<Resource<User>> =
        repository.login(loginParam)

    override suspend fun getAllStories(token: String, storiesParam: StoriesParam): Flow<Resource<List<Story>>> =
        repository.getAllStories(token, storiesParam)

    override suspend fun addStory(addStoryParam: AddStoryParam): Flow<Resource<Boolean>> =
        repository.addStory(addStoryParam)
}