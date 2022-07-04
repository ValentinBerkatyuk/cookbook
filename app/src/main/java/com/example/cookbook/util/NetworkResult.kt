package com.example.cookbook.util

sealed class NetworkResult<T>(
    val data: T? = null,
    val massage: String? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)

}