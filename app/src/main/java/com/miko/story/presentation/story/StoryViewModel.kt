package com.miko.story.presentation.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miko.story.domain.StoryUseCase
import com.miko.story.domain.model.AddStoryParam
import com.miko.story.domain.model.StoriesParam
import com.miko.story.domain.model.Story
import com.miko.story.domain.util.Resource
import com.miko.story.utils.collectResult

class StoryViewModel(private val storyUseCase: StoryUseCase) : ViewModel() {
    private val _storiesResult: MutableLiveData<Resource<List<Story>>> = MutableLiveData()
    private val _uploadResult: MutableLiveData<Resource<Boolean>> = MutableLiveData()

    val storiesResult: LiveData<Resource<List<Story>>> get() = _storiesResult
    val uploadResult: LiveData<Resource<Boolean>> get() = _uploadResult

    init {
        _storiesResult.value = Resource.Loading()
    }

    fun getAllStories(token: String, storiesParam: StoriesParam = StoriesParam()) {
        _storiesResult.value = Resource.Loading()
        viewModelScope.collectResult(_storiesResult) {
            storyUseCase.getAllStories(token, storiesParam)
        }
    }

    fun addStory(addStoryParam: AddStoryParam) {
        _uploadResult.value = Resource.Loading()
        viewModelScope.collectResult(_uploadResult) {
            storyUseCase.addStory(addStoryParam)
        }
    }
}