package com.miko.story.domain

import com.miko.story.data.StoryRepository

class StoryInteractor(private val repository: StoryRepository): StoryUseCase {
}