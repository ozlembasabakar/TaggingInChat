package com.example.tagginginchat.data

import com.example.tagginginchat.data.model.Message
import com.example.tagginginchat.data.model.User
import javax.inject.Inject

class Repository @Inject constructor(
    private val dataSource: DataSource,
) {

    fun getAllUser(): List<User> {
        return dataSource.users
    }

    fun getAllMessages(): List<Message> {
        return dataSource.messages
    }

    fun addNewMessageAndGetAllMessaged(message: Message): MutableList<Message> {
        dataSource.messages.add(message)
        return dataSource.messages
    }
}