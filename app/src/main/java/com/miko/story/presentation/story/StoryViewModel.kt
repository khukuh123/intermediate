package com.miko.story.presentation.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.miko.story.base.BasePagingSource
import com.miko.story.domain.StoryUseCase
import com.miko.story.domain.model.AddStoryParam
import com.miko.story.domain.model.StoriesParam
import com.miko.story.domain.model.Story
import com.miko.story.domain.util.Resource
import com.miko.story.utils.collectResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class StoryViewModel(private val storyUseCase: StoryUseCase) : ViewModel() {
    private val _storiesResult: MutableLiveData<PagingData<Story>> = MutableLiveData<PagingData<Story>>().apply {
        cachedIn(viewModelScope)
    }
    private val _uploadResult: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    private val _mapsResult: MutableLiveData<Resource<List<Story>>> = MutableLiveData()

    val storiesResult: LiveData<PagingData<Story>> get() = _storiesResult
    val uploadResult: LiveData<Resource<Boolean>> get() = _uploadResult
    val mapsResult: LiveData<Resource<List<Story>>> get() = _mapsResult

    init {
        _mapsResult.value = Resource.Loading()
    }

    fun getAllStories(token: String, storiesParam: StoriesParam = StoriesParam()) {
        Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                object : BasePagingSource<Story>() {
                    override suspend fun call(position: Int, loadSize: Int): Flow<Resource<List<Story>>> =
                        storyUseCase.getAllStories(token, storiesParam.copy(size = loadSize, page = position))
                }
            }
        ).flow.let {
            viewModelScope.launch {
                it.collect {
                    _storiesResult.postValue(it)
                }
            }
        }
    }

    fun addStory(addStoryParam: AddStoryParam) {
        _uploadResult.value = Resource.Loading()
        viewModelScope.collectResult(_uploadResult) {
            storyUseCase.addStory(addStoryParam)
        }
    }

    fun getStoriesMaps(token: String, storiesParam: StoriesParam) {
        _mapsResult.value = Resource.Loading()
        viewModelScope.collectResult(_mapsResult) {
            storyUseCase.getAllStories(token, storiesParam)
        }
    }
}