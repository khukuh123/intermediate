package com.miko.story.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.miko.story.DummyData
import com.miko.story.MainCoroutineRule
import com.miko.story.data.StoryRepository
import com.miko.story.domain.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class StoryInteractorTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyInteractor: StoryInteractor

    @Before
    fun setUp() {
        storyInteractor = StoryInteractor(storyRepository)
    }

    @Test
    fun `when register() called and success return non-null and true value`() = runTest {
        val expectedValue = Resource.Success(true)
        val flow = flow {
            emit(Resource.Loading())
            delay(1000)
            emit(expectedValue)
        }
        Mockito.`when`(storyRepository.register(DummyData.registerParam)).thenReturn(flow)
        storyInteractor.register(DummyData.registerParam).collectIndexed { i, actualValue ->
            when (i) {
                0 -> {
                    assertNull(actualValue.data)
                    assert(actualValue is Resource.Loading)
                }
                1 -> {
                    assertNotNull(actualValue.data)
                    assert(actualValue is Resource.Success)
                    assert(expectedValue.data!! == actualValue.data!!)
                    Mockito.verify(storyRepository).register(DummyData.registerParam)
                }
            }
        }
    }

    @Test
    fun `when login() called and success return non-null and same content of user`() = runTest {
        val expectedValue = Resource.Success(DummyData.user)
        val flow = flow {
            emit(Resource.Loading())
            delay(1000)
            emit(expectedValue)
        }
        Mockito.`when`(storyRepository.login(DummyData.loginParam)).thenReturn(flow)
        storyInteractor.login(DummyData.loginParam).collectIndexed { i, actualValue ->
            when (i) {
                0 -> {
                    assertNull(actualValue.data)
                    assert(actualValue is Resource.Loading)
                }
                1 -> {
                    assertNotNull(actualValue.data)
                    assert(actualValue is Resource.Success)
                    assert(expectedValue.data!! == actualValue.data!!)
                    Mockito.verify(storyRepository).login(DummyData.loginParam)
                }
            }
        }
    }

    @Test
    fun `when getAllStories() called and success then return non-null, same size, and same content of stories`() = runTest {
        val expectedValue = Resource.Success(DummyData.stories)
        val flow = flow {
            emit(Resource.Loading())
            delay(1000)
            emit(expectedValue)
        }
        Mockito.`when`(storyRepository.getAllStories(DummyData.token, DummyData.storiesParam)).thenReturn(flow)
        storyInteractor.getAllStories(DummyData.token, DummyData.storiesParam).collectIndexed { i, actualValue ->
            when (i) {
                0 -> {
                    assertNull(actualValue.data)
                    assert(actualValue is Resource.Loading)
                }
                1 -> {
                    assertNotNull(actualValue.data)
                    assert(actualValue is Resource.Success)
                    assert(expectedValue.data!!.size == actualValue.data!!.size)
                    assert(expectedValue.data!!.toTypedArray() contentDeepEquals actualValue.data!!.toTypedArray())
                    Mockito.verify(storyRepository).getAllStories(DummyData.token, DummyData.storiesParam)
                }
            }
        }
    }

    @Test
    fun addStory() = runTest {
        val expectedValue = Resource.Success(true)
        val flow = flow {
            emit(Resource.Loading())
            delay(1000)
            emit(expectedValue)
        }
        Mockito.`when`(storyRepository.addStory(DummyData.addStoriesParam)).thenReturn(flow)
        storyInteractor.addStory(DummyData.addStoriesParam).collectIndexed { i, actualValue ->
            when (i) {
                0 -> {
                    assertNull(actualValue.data)
                    assert(actualValue is Resource.Loading)
                }
                1 -> {
                    assertNotNull(actualValue.data)
                    assert(actualValue is Resource.Success)
                    assert(expectedValue.data!! == actualValue.data!!)
                    Mockito.verify(storyRepository).addStory(DummyData.addStoriesParam)
                }
            }
        }
    }
}