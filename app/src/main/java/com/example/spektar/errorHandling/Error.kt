package com.example.spektar.errorHandling

sealed interface Error

enum class FetchError: Error {
    FAILED_RETRIEVAL
}