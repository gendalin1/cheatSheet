package com.example.cheatsheet.model

data class QuestionElement(
    val number: Int,
    val question: String,
    val answers: List<AnswerItem>
)