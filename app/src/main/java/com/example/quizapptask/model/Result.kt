package com.example.quizapptask.model

data class Result(
    val response_code: Int,
    val results: List<Question>
)