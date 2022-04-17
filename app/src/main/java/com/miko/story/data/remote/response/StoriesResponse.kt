package com.miko.story.data.remote.response


import com.google.gson.annotations.SerializedName

data class StoriesResponse(
    @SerializedName("error")
    val error: Boolean?,
    @SerializedName("listStory")
    val listStory: List<StoryItem>?,
    @SerializedName("message")
    val message: String?,
)