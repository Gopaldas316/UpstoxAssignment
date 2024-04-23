package com.example.upstoxassignment.models

sealed class RequestResult<out R> {
    data class Success<out T>(val data: T) : RequestResult<T>()
    data class Error(val message: String) : RequestResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$$message]"
        }
    }
}