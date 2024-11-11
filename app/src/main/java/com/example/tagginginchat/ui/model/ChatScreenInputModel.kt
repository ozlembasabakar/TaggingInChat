package com.example.tagginginchat.ui.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class ChatScreenInputModel(
    val message: MutableState<String> = mutableStateOf(""),
    val mentionedUser: MutableState<String> = mutableStateOf(""),
    val prevMentionedUsers: SnapshotStateList<String> = mutableStateListOf(),
    val showUserList: MutableState<Boolean> = mutableStateOf(false),
)