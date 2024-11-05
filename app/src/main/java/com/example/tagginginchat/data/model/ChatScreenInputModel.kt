package com.example.tagginginchat.data.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class ChatScreenInputModel(
    val message: MutableState<String> = mutableStateOf(""),
    val mentionedUser: MutableState<String> = mutableStateOf(""),
    val showUserList: MutableState<Boolean> = mutableStateOf(false),
)