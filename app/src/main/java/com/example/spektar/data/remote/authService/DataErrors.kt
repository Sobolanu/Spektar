package com.example.spektar.data.remote.authService

sealed interface DataFailure

interface DataUploadFailure : DataFailure {
    data object BadJson: DataUploadFailure
    data object BadJwt: DataUploadFailure
    data object UnexpectedFailure: UpdateFailure
    data object OverRequestRateLimit: DataUploadFailure
    data object RequestTimeout: DataUploadFailure
    data object DatabaseTimeout: DataUploadFailure

    data class ErrorOccurred(
        val cause: Throwable
    ) : DataUploadFailure
}

interface UpdateFailure : AuthFailure {
    data object SamePassword: UpdateFailure
    data object UnexpectedFailure: UpdateFailure
    data object RequestTimeout: UpdateFailure

    data class ErrorOccurred(
        val cause: Throwable
    ) : UpdateFailure
}