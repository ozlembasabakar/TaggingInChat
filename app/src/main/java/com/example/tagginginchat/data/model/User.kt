package com.example.tagginginchat.data.model

import androidx.compose.ui.graphics.Color

data class User(
    val id: Int,
    val name: String,
    val profileImage: Int,
    val color: Color,
)