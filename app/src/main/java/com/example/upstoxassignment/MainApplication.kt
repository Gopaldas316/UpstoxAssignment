package com.example.upstoxassignment

import android.app.Application
import com.example.upstoxassignment.api.HoldingsService
import com.example.upstoxassignment.api.RetrofitHelper
import com.example.upstoxassignment.repository.MainRepository
import com.google.android.material.transition.Hold
import retrofit2.Retrofit

class MainApplication : Application() {
    lateinit var mainRepository: MainRepository

    override fun onCreate() {
        super.onCreate()
        initiate()
    }

    private fun initiate() {
        val apiService = RetrofitHelper.getInstance().create(HoldingsService::class.java)
        mainRepository = MainRepository(apiService)
    }

}