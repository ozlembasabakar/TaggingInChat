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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tagginginchat.R
import com.example.tagginginchat.data.model.Message
import com.example.tagginginchat.data.model.User
import com.example.tagginginchat.ui.components.MessageBox
import com.example.tagginginchat.ui.components.TagLayout
import com.example.tagginginchat.ui.theme.Background
import com.example.tagginginchat.ui.theme.SendIconBackground
import com.example.tagginginchat.ui.theme.TagLayoutBackground
import com.example.tagginginchat.ui.theme.TaggingInChatTheme
import com.example.tagginginchat.ui.theme.Typography

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen() {

    var message by remember {
        mutableStateOf("")
    }

    var mentionedUser by remember {
        mutableStateOf("")
    }

    val focusManager = LocalFocusManager.current


    val chatScreenViewModel: ChatScreenViewModel = hiltViewModel()
    val viewState by chatScreenViewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .navigationBarsPadding()
            .statusBarsPadding()
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
        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            AnimatedVisibility(
                visible = message.contains("@"),
            ) {
                TagLayout(
                    users = viewState.users.filter { user ->
                        val searchText = message.substringAfterLast("@")
                        user.name.contains(searchText, ignoreCase = true)
                    },
                    searchedText = message.substringAfterLast("@"),
                ) {
                    mentionedUser = it.name
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 2.dp, start = 2.dp, end = 2.dp, top = 0.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                val annotatedString = buildAnnotatedString {
                    append(message)
                    // Highlight tags in message
                    val tagMatches = "@\\w+".toRegex().findAll(message)
                    tagMatches.forEach { matchResult ->
                        addStyle(
                            style = SpanStyle(color = Color.Blue),
                            start = matchResult.range.first,
                            end = matchResult.range.last + mentionedUser.length + 1
                        )
                    }
                }
                TextField(
                    textStyle = Typography.bodySmall,
                    value = TextFieldValue(
                        text = message,
                        selection = TextRange(message.length),
                    ),
                    onValueChange = { input ->
                        message = input.text
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
                                topStart = if (message.contains("@")) 0.dp else 36.dp,
                                topEnd = if (message.contains("@")) 0.dp else 36.dp,
                                bottomStart = 36.dp,
                                bottomEnd = 36.dp
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
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewChatScreen() {
    TaggingInChatTheme {
        ChatScreen()
    }
}