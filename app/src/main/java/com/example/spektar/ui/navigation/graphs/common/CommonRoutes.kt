package com.example.spektar.ui.navigation.graphs.common

import kotlinx.serialization.Serializable

@Serializable data object Common
@Serializable data class AppErrorScreen (val errorMessage: String)
@Serializable object ProfileScreen