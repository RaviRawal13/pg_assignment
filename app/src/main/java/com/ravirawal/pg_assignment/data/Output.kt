package com.ravirawal.pg_assignment.data

/**
 * A Sealed class to fetch data from server which will be either data or the error.
 */
sealed class Output<T> {
    data class Success<T>(val data: T) : Output<T>()
    data class Error<T>(val message: String) : Output<T>()
}
