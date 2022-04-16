package com.miko.story.data

import com.miko.story.data.remote.StoryApiClient

class StoryDataStore(private val api: StoryApiClient): StoryRepository {
}