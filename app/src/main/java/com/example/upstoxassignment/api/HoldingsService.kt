package com.example.upstoxassignment.api

import com.example.upstoxassignment.models.HoldingsData
import retrofit2.Response
import retrofit2.http.GET

interface HoldingsService {

    @GET("holdings")
    suspend fun getHoldingsData() : Response<HoldingsData>
}