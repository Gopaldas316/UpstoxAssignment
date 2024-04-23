package com.example.upstoxassignment.utils

import com.example.upstoxassignment.models.HoldingsRecord
import com.example.upstoxassignment.models.UserHolding

fun getHoldingsRecord(resp: List<UserHolding>): List<HoldingsRecord> {
    val listOfHoldingRecord = arrayListOf<HoldingsRecord>()
    for (holding in resp) {
        listOfHoldingRecord.add(
            HoldingsRecord(
                symbol = holding.symbol,
                netQty = holding.quantity,
                ltp = holding.ltp,
                pnl = getPNLCalc(holding),
                avgPrice = holding.avgPrice,
                close = holding.close
            )
        )
    }

    return listOfHoldingRecord
}

fun getPNLCalc(holding: UserHolding) : Double {
    val currentVal = holding.ltp * holding.quantity
    val totalInv = holding.avgPrice * holding.quantity

    return currentVal - totalInv
}