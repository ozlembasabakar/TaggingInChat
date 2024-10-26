package com.example.tagginginchat.ui

import androidx.lifecycle.ViewModel
import com.example.tagginginchat.data.Repository
import com.example.tagginginchat.data.model.Message
import com.example.tagginginchat.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _state = MutableStateFlow(ChatScreenViewState(repository.getAllMessages(), repository.getAllUser()))
    val state = _state.asStateFlow()

    fun sendMessage(message: Message) {
        _state.update { currentState ->
            val updatedMessages = currentState.messageList.toMutableList().apply {
                add(
                    Message(
                        isSent = message.isSent,
                        userId = message.userId,
                        content = message.content
                    )
                )
            }
            currentState.copy(messageList = updatedMessages)
        }
    }

    fun addNewMember(message: String) {
        val newMessage = Message(
            isSent = true,
            userId = 1,
            content = message
        )
        val newMessageList: MutableList<Message> = repository.addNewMessageAndGetAllMessaged(newMessage)
        _state.update { it.copy(messageList = newMessageList) }
    }
}

data class ChatScreenViewState(
    val messageList: List<Message>,
    val users: List<User>
)


