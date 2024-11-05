package com.example.tagginginchat.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tagginginchat.R
import com.example.tagginginchat.data.model.ChatScreenInputModel
import com.example.tagginginchat.data.model.Message
import com.example.tagginginchat.ui.components.MessageBox
import com.example.tagginginchat.ui.components.TagLayout
import com.example.tagginginchat.ui.theme.Background
import com.example.tagginginchat.ui.theme.ChatScreenBottomPadding
import com.example.tagginginchat.ui.theme.ChatScreenMessageListVerticalPadding
import com.example.tagginginchat.ui.theme.ChatScreenSendButtonPadding
import com.example.tagginginchat.ui.theme.ChatScreenSendButtonSize
import com.example.tagginginchat.ui.theme.ChatScreenTextFieldBottomPadding
import com.example.tagginginchat.ui.theme.ChatScreenTextFieldHeight
import com.example.tagginginchat.ui.theme.ChatScreenTextFieldHorizontalSpace
import com.example.tagginginchat.ui.theme.ChatScreenTextFieldPadding
import com.example.tagginginchat.ui.theme.ChatScreenTextFieldTopCornerRadiusWhenListDisplayed
import com.example.tagginginchat.ui.theme.ChatScreenTextFieldTopCornerRadiusWhenListNotDisplayed
import com.example.tagginginchat.ui.theme.MentionedUserTextColor
import com.example.tagginginchat.ui.theme.SendIconBackground
import com.example.tagginginchat.ui.theme.TagLayoutBackground
import com.example.tagginginchat.ui.theme.TaggingInChatTheme
import com.example.tagginginchat.ui.theme.Typography

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    scrollState: LazyListState,
    chatScreenInputModel: ChatScreenInputModel,
    viewState: ChatScreenViewState,
    sendMessage: (Message) -> Unit,
    receivedMessage: (Message) -> Unit,
) {

    val focusManager = LocalFocusManager.current
    val localDensity = LocalDensity.current
    var keyboardHeight by remember {
        mutableStateOf(0.dp)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .navigationBarsPadding()
            .statusBarsPadding()
            .padding(bottom = ChatScreenBottomPadding)
            .imePadding()
    ) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .wrapContentSize()
                .background(Background)
                .padding(vertical = ChatScreenMessageListVerticalPadding)
                .padding(bottom = keyboardHeight)
        ) {
            items(viewState.messageList) { message ->
                MessageBox(modifier = Modifier, message = message, viewState.users)
            }
        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            AnimatedVisibility(
                visible = chatScreenInputModel.showUserList.value,
            ) {
                TagLayout(
                    users = viewState.users.filter { user ->
                        val searchText = chatScreenInputModel.message.value.substringAfterLast("@")
                        user.name.contains(searchText, ignoreCase = true)
                    },
                    searchedText = chatScreenInputModel.message.value.substringAfterLast("@"),
                ) { selectedUser ->
                    chatScreenInputModel.mentionedUser.value = selectedUser.name
                    chatScreenInputModel.message.value =
                        chatScreenInputModel.message.value.substringBeforeLast("@") + "@" + selectedUser.name + " "
                    chatScreenInputModel.showUserList.value = false
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ChatScreenTextFieldHeight)
                    .padding(
                        bottom = ChatScreenTextFieldPadding,
                        start = ChatScreenTextFieldPadding,
                        end = ChatScreenTextFieldPadding,
                        top = ChatScreenTextFieldBottomPadding
                    )
                    .onGloballyPositioned { coordinates ->
                        keyboardHeight = with(localDensity) { coordinates.size.height.toDp() }
                    },
                horizontalArrangement = Arrangement.spacedBy(ChatScreenTextFieldHorizontalSpace),
                verticalAlignment = Alignment.CenterVertically
            ) {

                val annotatedString = buildAnnotatedString {
                    append(chatScreenInputModel.message.value)
                    val tagMatches = "@[\\w.]+(?:\\s[\\w.]+)*".toRegex()
                        .findAll(chatScreenInputModel.message.value)
                    tagMatches.forEach { matchResult ->
                        addStyle(
                            style = SpanStyle(color = MentionedUserTextColor),
                            start = matchResult.range.first,
                            end = matchResult.range.last + 1
                        )
                    }
                }
                TextField(
                    textStyle = Typography.bodySmall,
                    value = TextFieldValue(
                        text = chatScreenInputModel.message.value,
                        selection = TextRange(chatScreenInputModel.message.value.length),
                    ),
                    onValueChange = { input ->
                        chatScreenInputModel.message.value = input.text
                        chatScreenInputModel.showUserList.value =
                            chatScreenInputModel.message.value.contains("@")
                    },
                    visualTransformation = { textFieldValue ->
                        TransformedText(
                            text = annotatedString,
                            offsetMapping = object : OffsetMapping {
                                override fun originalToTransformed(offset: Int): Int = offset
                                override fun transformedToOriginal(offset: Int): Int = offset
                            }
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .clip(
                            RoundedCornerShape(
                                topStart = if (chatScreenInputModel.showUserList.value) ChatScreenTextFieldTopCornerRadiusWhenListNotDisplayed else ChatScreenTextFieldTopCornerRadiusWhenListDisplayed,
                                topEnd = if (chatScreenInputModel.showUserList.value) ChatScreenTextFieldTopCornerRadiusWhenListNotDisplayed else ChatScreenTextFieldTopCornerRadiusWhenListDisplayed,
                                bottomStart = ChatScreenTextFieldTopCornerRadiusWhenListDisplayed,
                                bottomEnd = ChatScreenTextFieldTopCornerRadiusWhenListDisplayed
                            )
                        ),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = TagLayoutBackground,
                        unfocusedContainerColor = TagLayoutBackground,
                        disabledContainerColor = TagLayoutBackground,
                        cursorColor = SendIconBackground,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Message",
                            color = Color.LightGray,
                            style = Typography.bodySmall
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.None
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    shape = RectangleShape,
                )
                Box(
                    modifier = Modifier
                        .size(ChatScreenSendButtonSize)
                        .clip(CircleShape)
                        .background(SendIconBackground)
                        .padding(ChatScreenSendButtonPadding)
                        .clickable {
                            if (chatScreenInputModel.message.value.isNotBlank()) {
                                sendMessage(
                                    Message(
                                        isSent = true,
                                        userId = 1,
                                        content = chatScreenInputModel.message.value
                                    )
                                )
                                if (chatScreenInputModel.mentionedUser.value.isNotBlank()) {
                                    receivedMessage(
                                        Message(
                                            isSent = false,
                                            userId = viewState.users
                                                .filter { it.name == chatScreenInputModel.mentionedUser.value }
                                                .first().id,
                                            content = "Of course!"
                                        )
                                    )
                                }
                                chatScreenInputModel.message.value = ""
                                chatScreenInputModel.mentionedUser.value = ""
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
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewChatScreen() {
    TaggingInChatTheme {

        val chatScreenViewModel: ChatScreenViewModel = hiltViewModel()
        val viewState by chatScreenViewModel.state.collectAsStateWithLifecycle()

        ChatScreen(
            scrollState = rememberLazyListState(),
            viewState = viewState,
            chatScreenInputModel = chatScreenViewModel.chatScreenInputModel,
            sendMessage = {},
            receivedMessage = {}
        )
    }
}