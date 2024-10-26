package com.example.tagginginchat.data.model

data class Message(
    val isSent: Boolean = true,
    val userId: Int,
    val content: String,
)