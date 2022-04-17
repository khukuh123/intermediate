package com.miko.story.domain.model

import java.io.File

data class AddStoryParam(
    val description: String,
    val image: File,
    val token: String
)
