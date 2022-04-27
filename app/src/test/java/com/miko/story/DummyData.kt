package com.miko.story

import com.google.android.gms.maps.model.LatLng
import com.miko.story.data.remote.response.*
import com.miko.story.domain.model.*
import java.io.File

object DummyData {
    const val token = "q1w2e3r4t5y6"
    private const val name = "John Doe"
    private const val email = "john.doe@gmail.com"
    private const val password = "johndoe123"

    val loginParam = LoginParam(email, password)
    val registerParam = RegisterParam(name, email, password)
    val storiesParam = StoriesParam(size = 10, page = 1)
    val mapsParam = StoriesParam(location = true, size = 10, page = 1)
    val addStoriesParam = AddStoryParam("This is description", File("/sdcard/1.png"), token)

    val user = User("1", name, token)
    val stories = Array(3) {
        Story(
            "$it",
            "http://asd.com/$it.png",
            "Story $it",
            "Description $it",
            LatLng(it * 10.00, it * 15.00))
    }.toList()

    val baseResponse = BaseResponse(false, "Success")
    private val loginResult = LoginResult(name, token, "1")
    val loginResponse = LoginResponse(false, "Success", loginResult)
    private val storyItems = Array(3) {
        StoryItem(
            "1234",
            "This is description",
            "$it",
            1.00,
            2.00,
            "Story #$it",
            "http://story.com/$it.png"
        )
    }.toList()
    val storiesResponse = StoriesResponse(false, storyItems, "Success")
}