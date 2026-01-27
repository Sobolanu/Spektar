package com.example.spektar.ui.navigation.graphs.common

import kotlinx.serialization.Serializable

@Serializable data class AppErrorScreen (val errorMessage: String)
@Serializable object ProfileScreen

@Serializable object HomeScreen

@Serializable object QuestionnaireScreen