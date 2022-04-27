package com.miko.story.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.miko.story.DummyData
import com.miko.story.MainCoroutineRule
import com.miko.story.data.remote.RemoteDataSource
import com.miko.story.data.util.ApiResult
import com.miko.story.domain.util.Resource
import com.miko.story.domain.util.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class StoryDataStoreTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var storyDataStore: StoryDataStore

    @Before
    fun setUp() {
        storyDataStore = StoryDataStore(remoteDataSource)
    }

    @Test
    fun `when register() called and success then return non-null and true value`() = runTest {
        val expectedValue = ApiResult.Success(DummyData.baseResponse)
        Mockito.`when`(remoteDataSource.register(DummyData.registerParam)).thenReturn(flowOf(expectedValue))
        val actualValue = storyDataStore.register(DummyData.registerParam).single()
        Mockito.verify(remoteDataSource).register(DummyData.registerParam)
        assertNotNull(actualValue.data!!)
        assert(actualValue is Resource.Success)
        assert(actualValue.data!! == expectedValue.result!!.map())
    }

    @Test
    fun `when login() called and success then return non-null, and same content of user`() = runTest {
        val expectedValue = ApiResult.Success(DummyData.loginResponse)
        Mockito.`when`(remoteDataSource.login(DummyData.loginParam)).thenReturn(flowOf(expectedValue))
        val actualValue = storyDataStore.login(DummyData.loginParam).single()
        Mockito.verify(remoteDataSource).login(DummyData.loginParam)
        assertNotNull(actualValue.data!!)
        assert(actualValue is Resource.Success)
        assert(actualValue.data!! == expectedValue.result?.loginResult!!.map())
    }

    @Test
    fun `when getAllStories() called and success then return non-null, same size, and same content of stories`() = runTest {
        val expectedValue = ApiResult.Success(DummyData.storiesResponse)
        Mockito.`when`(remoteDataSource.getAllStories(DummyData.token, DummyData.storiesParam)).thenReturn(flowOf(expectedValue))
        val actualValue = storyDataStore.getAllStories(DummyData.token, DummyData.storiesParam).single()
        Mockito.verify(remoteDataSource).getAllStories(DummyData.token, DummyData.storiesParam)
        assertNotNull(actualValue.data!!)
        assert(actualValue is Resource.Success)
        assert(actualValue.data!!.size == expectedValue.result?.listStory!!.size)
        assert(actualValue.data!!.toTypedArray() contentDeepEquals expectedValue.result!!.listStory!!.map { it.map() }.toTypedArray())
    }

    @Test
    fun `when addStory() called and success then return non-null and true value`() = runTest {
        val expectedValue = ApiResult.Success(DummyData.baseResponse)
        Mockito.`when`(remoteDataSource.addStory(DummyData.addStoriesParam)).thenReturn(flowOf(expectedValue))
        val actualValue = storyDataStore.addStory(DummyData.addStoriesParam).single()
        Mockito.verify(remoteDataSource).addStory(DummyData.addStoriesParam)
        assertNotNull(actualValue.data!!)
        assert(actualValue is Resource.Success)
        assert(actualValue.data!! == expectedValue.result!!.map())
    }
}