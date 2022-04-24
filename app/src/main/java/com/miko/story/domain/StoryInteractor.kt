package com.miko.story.domain

import com.miko.story.data.StoryRepository
import com.miko.story.data.util.ApiResult
import com.miko.story.domain.model.*
import com.miko.story.domain.util.Resource
import com.miko.story.domain.util.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoryInteractor(private val repository: StoryRepository) : StoryUseCase {
    override suspend fun register(registerParam: RegisterParam): Flow<Resource<Boolean>> =
        repository.register(registerParam).mapToDomain {
            true
        }

    override suspend fun login(loginParam: LoginParam): Flow<Resource<User>> =
        repository.login(loginParam).mapToDomain {
            it.loginResult?.map()!!
        }

    override suspend fun getAllStories(token: String, storiesParam: StoriesParam): Flow<Resource<List<Story>>> =
        repository.getAllStories(token, storiesParam).mapToDomain { response ->
            response.listStory?.map { it.map() }.orEmpty()
        }

    override suspend fun addStory(addStoryParam: AddStoryParam): Flow<Resource<Boolean>> =
        repository.addStory(addStoryParam).mapToDomain {
            true
        }

    private fun <T, U> Flow<ApiResult<T>>.mapToDomain(mapper: (T) -> U): Flow<Resource<U>> =
        this.map {
            when (it) {
                is ApiResult.Success -> {
                    Resource.Success(mapper.invoke(it.result!!))
                }
                else -> {
                    Resource.Error(it.errorCode ?: 999, it.errorMessage ?: "")
                }
            }
        }
}