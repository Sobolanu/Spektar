package com.example.spektar.data.remote.authService

sealed interface AuthFailure

interface SessionFailure : AuthFailure {
    data object SessionExpired: SessionFailure
    data object SessionNotFound: SessionFailure

    data object UnexpectedFailure: SessionFailure
    data object RequestTimeout: SessionFailure

    data class ErrorOccurred(
        val cause: Throwable
    ) : SessionFailure
}

interface UserAuthFailure : AuthFailure {
    // for sign up:
    data object WeakPassword: UserAuthFailure
    data object UserAlreadyExists: UserAuthFailure
    data object EmailExists: UserAuthFailure

    // for sign in
    data object UserNotFound: UserAuthFailure
    data object EmailNotConfirmed: UserAuthFailure
    data object EmailAddressInvalid: UserAuthFailure
    data object InvalidCredentials: UserAuthFailure

    // and general case:
    data object UnexpectedFailure: UserAuthFailure
    data object RequestTimeout: UserAuthFailure
    data object ValidationFailed: UserAuthFailure

    data class ErrorOccurred(
        val cause: Throwable
    ) : UserAuthFailure
}