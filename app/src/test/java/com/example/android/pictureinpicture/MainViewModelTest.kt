package com.example.android.pictureinpicture

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.pictureinpicture.repository.TimeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: MainViewModel
    @Mock
    private lateinit var mockRepository: TimeRepository

    private lateinit var testClock: TestClock

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        `when`(mockRepository.getTimeMillis()).thenReturn(MutableStateFlow(0L))
        `when`(mockRepository.getStarted()).thenReturn(MutableStateFlow(false))

        testClock = TestClock()
        viewModel = MainViewModel(mockRepository, testClock)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    // Test with LiveData
//    @Test
//    fun testStartOrPause() = runTest {
//        viewModel.started.observeForever(startedObserver)
//        viewModel.started.value
//
//        // Set the clock time
//        testClock.setTime(1000L)
//
//        // Start the timer
//        viewModel.startOrPause()
//        // Pause the timer
//        viewModel.startOrPause()
//
//        // Verify the sequence of changes
//        val inOrder: InOrder = inOrder(startedObserver)
//        inOrder.verify(startedObserver).onChanged(true)
//        inOrder.verify(startedObserver).onChanged(false)
//    }



    @Test
    fun `startOrPause starts the timer`() = runTest {
        val startedFlow = MutableStateFlow(false)
        `when`(mockRepository.getStarted()).thenReturn(startedFlow)

        viewModel.startOrPause()

        assert(startedFlow.value)
        verify(mockRepository).setStarted(true)
    }

    @Test
    fun `test clearTimer`() = runTest {
        viewModel.clear()
        verify(mockRepository).setTimeMillis(0L)
    }
}