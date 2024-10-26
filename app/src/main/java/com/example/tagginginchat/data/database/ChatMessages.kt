package com.example.tagginginchat.data.database

import com.example.tagginginchat.data.model.Message

val messages = mutableListOf(
    Message(
        isSent = true,
        userId = 1,
        content = "I'm Ready, I'm Ready, I'm Ready!"
    ), Message(
        isSent = false,
        userId = 2,
        content = "I Wumbo, You Wumbo, He, She, We Wumbo. Wumboing, Wumbology, the Study of Wumbo!"
    ), Message(
        isSent = false,
        userId = 3,
        content = "The Krusty Krab Pizza is the Pizza for You and Me!"
    )
)