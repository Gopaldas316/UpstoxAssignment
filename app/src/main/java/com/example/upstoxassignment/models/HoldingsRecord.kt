package com.example.upstoxassignment.models

data class HoldingsRecord(
    val symbol: String,
    val netQty : Int,
    val ltp: Double,
    val pnl: Double,
    val avgPrice : Double,
    val close : Double
)
