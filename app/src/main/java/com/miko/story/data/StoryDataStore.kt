package com.miko.story.data

import com.miko.story.data.remote.IRemoteDataSource
import com.miko.story.data.util.ApiResult
import com.miko.story.domain.model.*
import com.miko.story.domain.util.Resource
import com.miko.story.domain.util.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoryDataStore(private val remote: IRemoteDataSource) : StoryRepository {
    override suspend fun register(registerParam: RegisterParam): Flow<Resource<Boolean>> =
        remote.register(registerParam).mapToDomain {
            it.map()
        }

    override suspend fun login(loginParam: LoginParam): Flow<Resource<User>> =
        remote.login(loginParam).mapToDomain {
            it.loginResult?.map()!!
        }

    override suspend fun getAllStories(token: String, storiesParam: StoriesParam): Flow<Resource<List<Story>>> =
        remote.getAllStories(token, storiesParam).mapToDomain { response ->
            response.listStory?.map { it.map() }.orEmpty()
        }

    override suspend fun addStory(addStoryParam: AddStoryParam): Flow<Resource<Boolean>> =
        remote.addStory(addStoryParam).mapToDomain {
            it.map()
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
