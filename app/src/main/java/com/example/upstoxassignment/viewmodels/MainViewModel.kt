package com.example.upstoxassignment.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upstoxassignment.models.HoldingsRecord
import com.example.upstoxassignment.models.RequestResult
import com.example.upstoxassignment.models.UiState
import com.example.upstoxassignment.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository?) : ViewModel() {
    private val _holdingsData = MutableLiveData<UiState>(UiState.Loading)
    val holdingsData: LiveData<UiState>
        get() = _holdingsData

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            try {
                val result = repository?.getHoldingsInfo()
                when (result) {
                    is RequestResult.Success -> {
                        _holdingsData.postValue(UiState.Success(result.data))
                    }

                    is RequestResult.Error -> {
                        _holdingsData.postValue(UiState.Error(result.message))
                    }

                    else -> {
                        //Do Nothing
                    }
                }
            } catch (ioException: Exception) {
                _holdingsData.postValue(UiState.Error(ioException.message.toString()))
            }

        }
    }

    fun getCurrentValue(): Double {
        var currVal = 0.0
        when (val num = holdingsData.value) {
            is UiState.Success -> {
                for (item in num.data) {
                    currVal += item.ltp * item.netQty
                }
            }

            else -> {
                // Do nothing
            }
        }
        return currVal
    }

    fun getTotalInvValue(): Double {
        var totalInvValue = 0.0
        when (val num = holdingsData.value) {
            is UiState.Success -> {
                for (item in num.data) {
                    totalInvValue += item.avgPrice * item.netQty
                }
            }

            else -> {
                // Do nothing
            }
        }
        return totalInvValue
    }

    fun getTotalPNLValue(): Double {
        return getCurrentValue() - getTotalInvValue()
    }

    fun getTodaysPNLValue(): Double {
        var todaysNPLVal = 0.0
        when (val num = holdingsData.value) {
            is UiState.Success -> {
                for (item in num.data) {
                    todaysNPLVal += (item.close - item.ltp) * item.netQty
                }
            }

            else -> {
                // Do nothing
            }
        }

        return todaysNPLVal
    }
}