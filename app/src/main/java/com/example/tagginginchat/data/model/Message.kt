package com.example.tagginginchat.data.model

data class Message(
    val chatGroupId: Int,
    val userId: Int,
    val content: String,
)