package com.cinetech.domain.models

sealed class Response<T> {
    data object Loading : Response<Nothing>()
    data class Success<T>(val result: T) : Response<T>()
    data class Error(val message: String?, val throwable: Throwable? = null) : Response<Nothing>()
    data object Timeout : Response<Nothing>()
}