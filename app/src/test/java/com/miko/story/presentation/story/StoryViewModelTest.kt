package com.miko.story.presentation.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.miko.story.DummyData
import com.miko.story.MainCoroutineRule
import com.miko.story.domain.StoryUseCase
import com.miko.story.domain.util.Resource
import com.miko.story.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var storyUseCase: StoryUseCase
    private lateinit var storyViewModel: StoryViewModel

    @Before
    fun setUp() {
        storyViewModel = StoryViewModel(storyUseCase)
    }

    @Test
    fun `when getAllStories called and success then return non-null value`() = runTest {
        val expectedValue = Resource.Success(DummyData.stories)
        val flow = flow {
            emit(expectedValue)
        }
        lenient().`when`(storyUseCase.getAllStories(DummyData.token, DummyData.storiesParam)).thenReturn(flow)
        storyViewModel.getAllStories(DummyData.token, DummyData.storiesParam)
        val actualValue = storyViewModel.storiesResult.getOrAwaitValue()
        assertNotNull(actualValue)
    }

    @Test
    fun `when addStory() called and success then return non-null and true value`() = runTest {
        val expectedValue = Resource.Success(true)
        val flow = flow {
            emit(Resource.Loading())
            delay(1000)
            emit(expectedValue)
        }
        `when`(storyUseCase.addStory(DummyData.addStoriesParam)).thenReturn(flow)
        storyViewModel.addStory(DummyData.addStoriesParam)
        var actualValue = storyViewModel.uploadResult.getOrAwaitValue()
        assertNull(actualValue.data)
        assert(actualValue is Resource.Loading)
        advanceUntilIdle()
        actualValue = storyViewModel.uploadResult.getOrAwaitValue()
        verify(storyUseCase).addStory(DummyData.addStoriesParam)
        assertNotNull(actualValue.data)
        assert(expectedValue.data!! == actualValue.data!!)
    }

    @Test
    fun `when getStoriesMaps() called and success then return non-null, same size, and same content of stories`() = runTest {
        val expectedValue = Resource.Success(DummyData.stories)
        val flow = flow {
            emit(Resource.Loading())
            delay(1000)
            emit(expectedValue)
        }
        `when`(storyUseCase.getAllStories(DummyData.token, DummyData.mapsParam)).thenReturn(flow)
        storyViewModel.getStoriesMaps(DummyData.token, DummyData.mapsParam)
        var actualValue = storyViewModel.mapsResult.getOrAwaitValue()
        assertNull(actualValue.data)
        assert(actualValue is Resource.Loading)
        advanceUntilIdle()
        actualValue = storyViewModel.mapsResult.getOrAwaitValue()
        verify(storyUseCase).getAllStories(DummyData.token, DummyData.mapsParam)
        assertNotNull(actualValue.data)
        assert(actualValue is Resource.Success)
        assert(actualValue.data!!.size == expectedValue.data!!.size)
        assert(actualValue.data!!.toTypedArray() contentDeepEquals expectedValue.data!!.toTypedArray())
    }
}