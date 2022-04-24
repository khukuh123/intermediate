package com.miko.story.data

import android.util.Log
import com.miko.story.data.remote.StoryApiClient
import com.miko.story.data.remote.response.BaseResponse
import com.miko.story.data.remote.response.LoginResponse
import com.miko.story.data.remote.response.StoriesResponse
import com.miko.story.data.util.ApiResult
import com.miko.story.domain.model.AddStoryParam
import com.miko.story.domain.model.LoginParam
import com.miko.story.domain.model.RegisterParam
import com.miko.story.domain.model.StoriesParam
import com.miko.story.domain.util.map
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

    override suspend fun getAllStories(token: String, storiesParam: StoriesParam): Flow<ApiResult<StoriesResponse>> {
        return api.getAllStories(token.addBearerToken(), storiesParam.map()).call()
    }

    override suspend fun addStory(addStoryParam: AddStoryParam): Flow<ApiResult<BaseResponse>> {
        val file = addStoryParam.image.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart = MultipartBody.Part.createFormData("photo", addStoryParam.image.name, file)
        return api.addStory(addStoryParam.token.addBearerToken(), addStoryParam.map(), imageMultipart).call()
    }

    private fun <T> Response<T>.call(): Flow<ApiResult<T>> =
        flow<ApiResult<T>> {
            try {
                Log.d("hitted", "Calling...")
                val response = this@call
                if (response.isSuccessful) {
                    Log.d("hitted", "Successs!!")
                    emit(ApiResult.Success(response.body()!!))
                } else {
                    Log.d("hitted", "Failed")
                    with(response) {
                        errorBody()?.string()?.let { value ->
                            val message = JSONObject(value).getString("message")
                            emit(ApiResult.Error(code(), message))
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("hitted", "Error")
                emit(ApiResult.Error(999, e.message.orEmpty()))
            }
        }.flowOn(Dispatchers.IO)

    private fun String.addBearerToken() =
        StringBuilder()
            .append("Bearer $this")
            .toString()
}
