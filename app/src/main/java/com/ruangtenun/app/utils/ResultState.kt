package com.ruangtenun.app.utils

sealed class ResultState<out R> private constructor() {
    data object Idle : ResultState<Nothing>()
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val error: String) : ResultState<Nothing>()
    data object Loading : ResultState<Nothing>()
}