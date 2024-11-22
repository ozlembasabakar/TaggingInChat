package com.example.tagginginchat.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagginginchat.data.Repository
import com.example.tagginginchat.data.model.Message
import com.example.tagginginchat.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _state = MutableStateFlow(ChatScreenViewState(messageList = repository.getAllMessages(), users = repository.getAllUser()))
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
            currentState.copy(
                messageList = updatedMessages,
                message = "",
                mentionedUser = mutableStateOf("")
            )
        }
    }

    fun receivedMessage(message: Message) {
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

    fun onSelectedUser(user: User) {
        _state.update { currentState ->
            currentState.copy(
                message = currentState.message.substringBeforeLast("@") + "@" + user.name + " ",
                mentionedUser = mutableStateOf(user.name),
                prevMentionedUsers = currentState.prevMentionedUsers.apply {
                    add(user.name)
                },
                showUserList = false
            )
        }
    }

    private fun filteredUser() {
        val lastAtIndex = _state.value.message.lastIndexOf('@')
        if (lastAtIndex != -1) {
            val textAfterAt = _state.value.message.substring(lastAtIndex + 1)
            _state.update { currentState ->
                currentState.copy(
                    users = repository.getAllUser().filter { user ->
                        user.name !in currentState.prevMentionedUsers &&
                                user.name.contains(textAfterAt, ignoreCase = true)
                    }
                )
            }
        }
    }

    fun onMessageChanged(input: String) {
        _state.update { currentState ->
            val lastAtIndex = input.lastIndexOf('@')
            val showUserList = if (lastAtIndex != -1) {
                val textAfterAt = input.substring(lastAtIndex + 1)
                !textAfterAt.contains(' ')
            } else false
            currentState.copy(
                message = input,
                showUserList = showUserList
            )
        }
        filteredUser()
    }
}

data class ChatScreenViewState(
    val messageList: List<Message>,
    val users: List<User>,
    val message: String = "",
    val mentionedUser: MutableState<String> = mutableStateOf(""),
    val prevMentionedUsers: SnapshotStateList<String> = mutableStateListOf(),
    val showUserList: Boolean = false,
)
