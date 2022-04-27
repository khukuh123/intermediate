package com.miko.story.presentation.membership

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
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MembershipViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var storyUseCase: StoryUseCase
    private lateinit var membershipViewModel: MembershipViewModel

    @Before
    fun setUp() {
        membershipViewModel = MembershipViewModel(storyUseCase)
    }

    @Test
    fun `when register() called and success then return non-null, and true value`() = runTest {
        val expectedValue = Resource.Success(true)
        val flow = flow {
            emit(Resource.Loading())
            delay(1000)
            emit(expectedValue)
        }
        `when`(storyUseCase.register(DummyData.registerParam)).thenReturn(flow)
        membershipViewModel.register(DummyData.registerParam)
        var actualValue = membershipViewModel.registerResult.getOrAwaitValue()
        assertNull(actualValue.data)
        assert(actualValue is Resource.Loading)
        advanceUntilIdle()
        actualValue = membershipViewModel.registerResult.getOrAwaitValue()
        verify(storyUseCase).register(DummyData.registerParam)
        assertNotNull(actualValue.data)
        assert(actualValue is Resource.Success)
        assert(actualValue.data!! == expectedValue.data!!)
    }

    @Test
    fun `when login() called then return non-null, and same content of user`() = runTest {
        val expectedValue = Resource.Success(DummyData.user)
        val flow = flow {
            emit(Resource.Loading())
            delay(1000)
            emit(expectedValue)
        }
        `when`(storyUseCase.login(DummyData.loginParam)).thenReturn(flow)
        membershipViewModel.login(DummyData.loginParam)
        var actualValue = membershipViewModel.loginResult.getOrAwaitValue()
        assertNull(actualValue.data)
        assert(actualValue is Resource.Loading)
        advanceUntilIdle()
        actualValue = membershipViewModel.loginResult.getOrAwaitValue()
        verify(storyUseCase).login(DummyData.loginParam)
        assertNotNull(actualValue.data)
        assert(actualValue is Resource.Success)
        assert(expectedValue.data!! == actualValue.data!!)
    }
}