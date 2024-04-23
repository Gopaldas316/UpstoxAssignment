package com.example.upstoxassignment.models

sealed class UiState {
    data object Loading : UiState()
    data class Success(val data : List<HoldingsRecord>) : UiState()
    data class Error(val message : String) : UiState()
}
