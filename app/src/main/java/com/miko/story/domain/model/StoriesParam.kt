package com.miko.story.domain.model

data class StoriesParam(
    val location: Boolean? = null,
    val size: Int = 5,
    val page: Int = 0,
)
