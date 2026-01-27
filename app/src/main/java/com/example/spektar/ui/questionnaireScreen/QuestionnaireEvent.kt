package com.example.spektar.ui.questionnaireScreen

sealed interface QuestionnaireEvent {
    object FinishQuestionnaire : QuestionnaireEvent
    data class UpdateEmbedding(val indexToUpdate: Int, val action: String) : QuestionnaireEvent
}