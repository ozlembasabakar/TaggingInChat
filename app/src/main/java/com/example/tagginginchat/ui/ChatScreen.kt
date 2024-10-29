package com.example.tagginginchat.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tagginginchat.R
import com.example.tagginginchat.data.model.Message
import com.example.tagginginchat.ui.components.MessageBox
import com.example.tagginginchat.ui.theme.Background
import com.example.tagginginchat.ui.theme.SendIconBackground
import com.example.tagginginchat.ui.theme.TagLayoutBackground
import com.example.tagginginchat.ui.theme.TaggingInChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen() {

    var message by remember {
        mutableStateOf("")
    }

    val focusManager = LocalFocusManager.current

    val chatScreenViewModel: ChatScreenViewModel = hiltViewModel()
    val viewState by chatScreenViewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Background)
                    .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
                    .padding(bottom = 2.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(Background)
                        .padding(vertical = 8.dp)
                ) {
                    items(viewState.messageList) { message ->
                        MessageBox(modifier = Modifier, message = message, viewState.users)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = message,
                        onValueChange = { input ->
                            message = input
                        },
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight()
                            .clip(RoundedCornerShape(16.dp))
                            .onKeyEvent { keyEvent ->
                                if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyDown) {
                                    if (message.isNotBlank()) {
                                        chatScreenViewModel.addNewMember(message)
                                        message = ""
                                    }
                                    true
                                } else {
                                    false
                                }
                            },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = TagLayoutBackground,
                            cursorColor = SendIconBackground,
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent

                        ),
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = "Message",
                                color = Color.LightGray
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.None
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                    )
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(SendIconBackground)
                            .padding(8.dp)
                            .clickable {
                                if (message.isNotBlank()) {
                                    chatScreenViewModel.sendMessage(
                                        Message(
                                            isSent = true,
                                            userId = 1,
                                            content = message
                                        )
                                    )
                                    message = ""
                                }
                            },
                        contentAlignment = Alignment.Center,

                        ) {
                        Icon(
                            painter = painterResource(id = R.drawable.send_icon),
                            contentDescription = "Send icon"
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingChatScreen() {
    TaggingInChatTheme {
        ChatScreen()
    }
}