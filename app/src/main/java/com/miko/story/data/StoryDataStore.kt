package com.miko.story.data

import com.miko.story.data.remote.StoryApiClient
import com.miko.story.data.remote.response.LoginResponse
import com.miko.story.data.remote.response.BaseResponse
import com.miko.story.data.remote.response.StoriesResponse
import com.miko.story.data.util.ApiResult
import com.miko.story.domain.model.AddStoryParam
import com.miko.story.domain.model.LoginParam
import com.miko.story.domain.model.RegisterParam
import com.miko.story.domain.util.StoryMapper.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response

class StoryDataStore(private val api: StoryApiClient) : StoryRepository {
    override suspend fun register(registerParam: RegisterParam): Flow<ApiResult<BaseResponse>> =
        api.register(registerParam.map()).call()

    override suspend fun login(loginParam: LoginParam): Flow<ApiResult<LoginResponse>> =
        api.login(loginParam.map()).call()

    override suspend fun getAllStories(token: String): Flow<ApiResult<StoriesResponse>>{
        val modifiedToken = "Bearer $token"
        return api.getAllStories(modifiedToken).call()
    }

    override suspend fun addStory(addStoryParam: AddStoryParam): Flow<ApiResult<BaseResponse>>{
        val description = addStoryParam.description.toRequestBody("text/plain".toMediaType())
        val file = addStoryParam.image.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart = MultipartBody.Part.createFormData("photo", addStoryParam.image.name, file)
        val modifiedToken = "Bearer ${addStoryParam.token}"
        return api.addStory(modifiedToken, description, imageMultipart).call()
    }

    private fun <T> Response<T>.call() =
        flow<ApiResult<T>> {
            try {
                val response = this@call
                if (response.isSuccessful) {
                    emit(ApiResult.Success(response.body()!!))
                } else {
                    with(response) {
                        errorBody()?.string()?.let { value ->
                            val message = JSONObject(value).getString("message")
                            emit(ApiResult.Error(code(), message))
                        }
                    }
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(999, e.message.orEmpty()))
            }
        }.flowOn(Dispatchers.IO)
}
