package com.example.upstoxassignment.repository

import com.example.upstoxassignment.api.HoldingsService
import com.example.upstoxassignment.models.HoldingsRecord
import com.example.upstoxassignment.models.RequestResult
import com.example.upstoxassignment.utils.getHoldingsRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(private val service: HoldingsService) {

    suspend fun getHoldingsInfo() : RequestResult<List<HoldingsRecord>> {
        return withContext(Dispatchers.IO) {
            val response = service.getHoldingsData()
            val result = if(response.isSuccessful) {
                RequestResult.Success(
                    getHoldingsRecord(response.body()!!.data.userHolding)
                )
            } else RequestResult.Error("Something Went Wrong!!!")
            result
        }
    }

}