package com.example.upstoxassignment.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.upstoxassignment.models.HoldingsRecord
import com.example.upstoxassignment.models.RequestResult
import com.example.upstoxassignment.models.UiState
import com.example.upstoxassignment.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private val testScope = TestCoroutineScope(testDispatcher)

    @Mock
    lateinit var mockRepository: MainRepository

    lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `test getData() with success`() = testScope.runTest {
        // Arrange
        val mockHoldingsData = listOf(
            HoldingsRecord("1", 3, 100.0, 150.0, 200.0, 50.0)
        )
        `when`(mockRepository.getHoldingsInfo()).thenReturn(RequestResult.Success(mockHoldingsData))

        // Act
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val expectedUiState = UiState.Success(mockHoldingsData)
        assertEquals(expectedUiState, viewModel.holdingsData.value)
    }

    @Test
    fun `test getData() with error`() = testScope.runTest {
        //Arrange
        `when`(mockRepository.getHoldingsInfo()).thenReturn(RequestResult.Error("Something went wrong"))

        // Act
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val expectedUiState = UiState.Error("Something went wrong")
        assertEquals(expectedUiState, viewModel.holdingsData.value)
    }

}