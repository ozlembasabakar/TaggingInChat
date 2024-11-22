package com.example.tagginginchat.ui.jetpackCompose

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.ripple.rememberRipple
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
import com.example.tagginginchat.data.model.Message
import com.example.tagginginchat.data.model.User
import com.example.tagginginchat.ui.ChatScreenViewModel
import com.example.tagginginchat.ui.ChatScreenViewState
import com.example.tagginginchat.ui.jetpackCompose.components.MessageBox
import com.example.tagginginchat.ui.jetpackCompose.components.TagLayout
import com.example.tagginginchat.ui.jetpackCompose.theme.Background
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenBottomPadding
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenMessageListVerticalPadding
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenSendButtonPadding
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenSendButtonSize
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenTextFieldHeight
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenTextFieldHorizontalSpace
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenTextFieldPadding
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenTextFieldTopCornerRadiusWhenListDisplayed
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenTextFieldTopCornerRadiusWhenListNotDisplayed
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenTextFieldTopPadding
import com.example.tagginginchat.ui.jetpackCompose.theme.MentionedUserTextColor
import com.example.tagginginchat.ui.jetpackCompose.theme.SendIconBackground
import com.example.tagginginchat.ui.jetpackCompose.theme.TagLayoutBackground
import com.example.tagginginchat.ui.jetpackCompose.theme.TaggingInChatTheme
import com.example.tagginginchat.ui.jetpackCompose.theme.Typography

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    scrollState: LazyListState,
    chatScreenViewState: ChatScreenViewState,
    onSelectedUser: (User) -> Unit,
    onMessageChanged: (String) -> Unit,
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
            items(chatScreenViewState.messageList) { message ->
                MessageBox(
                    modifier = Modifier,
                    message = message,
                    users = chatScreenViewState.users,
                    prevMentionedUsers = chatScreenViewState.prevMentionedUsers
                )
            }
        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            AnimatedVisibility(
                visible = chatScreenViewState.showUserList,
            ) {
                TagLayout(
                    users = chatScreenViewState.users,
                    searchedText = chatScreenViewState.message.substringAfterLast("@"),
                ) { selectedUser ->
                    onSelectedUser(selectedUser)
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
                        top = ChatScreenTextFieldTopPadding
                    )
                    .onGloballyPositioned { coordinates ->
                        keyboardHeight = with(localDensity) { coordinates.size.height.toDp() }
                    },
                horizontalArrangement = Arrangement.spacedBy(ChatScreenTextFieldHorizontalSpace),
                verticalAlignment = Alignment.CenterVertically
            ) {

                val annotatedString = buildAnnotatedString {
                    append(chatScreenViewState.message)
                    chatScreenViewState.prevMentionedUsers.forEach { user ->
                        val userMatches = "@$user".toRegex().findAll(chatScreenViewState.message)
                        userMatches.forEach { matchResult ->
                            addStyle(
                                style = SpanStyle(color = MentionedUserTextColor),
                                start = matchResult.range.first,
                                end = matchResult.range.last + 1
                            )
                        }
                    }
                }
                TextField(
                    textStyle = Typography.bodySmall,
                    value = TextFieldValue(
                        text = chatScreenViewState.message,
                        selection = TextRange(chatScreenViewState.message.length),
                    ),
                    onValueChange = { input ->
                        onMessageChanged(input.text)
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
                                topStart = if (chatScreenViewState.showUserList) ChatScreenTextFieldTopCornerRadiusWhenListNotDisplayed else ChatScreenTextFieldTopCornerRadiusWhenListDisplayed,
                                topEnd = if (chatScreenViewState.showUserList) ChatScreenTextFieldTopCornerRadiusWhenListNotDisplayed else ChatScreenTextFieldTopCornerRadiusWhenListDisplayed,
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
                        errorIndicatorColor = Color.Transparent,
                        selectionColors = TextSelectionColors(
                            handleColor = SendIconBackground,
                            backgroundColor = SendIconBackground
                        )
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
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication =
                            rememberRipple(bounded = true),
                            onClick = {
                                if (chatScreenViewState.message.isNotBlank()) {
                                    sendMessage(
                                        Message(
                                            isSent = true,
                                            userId = 1,
                                            content = chatScreenViewState.message
                                        )
                                    )
                                    if (chatScreenViewState.mentionedUser.value.isNotBlank()) {
                                        receivedMessage(
                                            Message(
                                                isSent = false,
                                                userId = chatScreenViewState.users
                                                    .filter { it.name == chatScreenViewState.mentionedUser.value }
                                                    .first().id,
                                                content = "Of course!"
                                            )
                                        )
                                    }
                                }
                            }
                        )
                        .background(SendIconBackground)
                        .padding(ChatScreenSendButtonPadding),
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
            chatScreenViewState = viewState,
            onSelectedUser = {},
            onMessageChanged = {},
            sendMessage = {},
            receivedMessage = {}
        )
    }
}