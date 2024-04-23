package com.example.upstoxassignment.repository

import com.example.upstoxassignment.api.HoldingsService
import com.example.upstoxassignment.models.Data
import com.example.upstoxassignment.models.HoldingsData
import com.example.upstoxassignment.models.RequestResult
import com.example.upstoxassignment.models.UserHolding
import com.example.upstoxassignment.utils.getHoldingsRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

class MainRepositoryTest {

    @Mock
    lateinit var mockService: HoldingsService

    lateinit var repository: MainRepository

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = MainRepository(mockService)
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
        Dispatchers.resetMain()
    }

    @Test
    fun `test getHoldingsInfo() with successful response`() = runBlocking {
        // Arrange
        val responseData = HoldingsData(
            data = Data(
                listOf(
                    UserHolding(
                        symbol = "HDFC",
                        avgPrice = 34.5,
                        close = 57.8,
                        ltp = 4569.4,
                        quantity = 4
                    ),
                    UserHolding(
                        symbol = "AXIS",
                        avgPrice = 54.6,
                        close = 60.2,
                        ltp = 459.4,
                        quantity = 5
                    )
                )
            )
        )
        val successResponse = Response.success(responseData)
        `when`(mockService.getHoldingsData()).thenReturn(successResponse)

        // Act
        val result = repository.getHoldingsInfo()

        // Assert
        assertEquals(
            RequestResult.Success(getHoldingsRecord(successResponse.body()!!.data.userHolding)),
            result
        )
    }
}