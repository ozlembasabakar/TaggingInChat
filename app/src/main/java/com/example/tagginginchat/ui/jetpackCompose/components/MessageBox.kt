package com.example.tagginginchat.ui.jetpackCompose.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.example.tagginginchat.data.DataSource
import com.example.tagginginchat.data.model.Message
import com.example.tagginginchat.data.model.User
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenMessageBoxContentBottomPadding
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenMessageBoxContentHorizontalPadding
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenMessageBoxContentTopPadding
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenMessageBoxImageEndPadding
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenMessageBoxImageWidth
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenMessageBoxNameBottomPadding
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenMessageBoxNameHorizontalPadding
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenMessageBoxNameTopPadding
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenMessageBoxPaddingHorizontal
import com.example.tagginginchat.ui.jetpackCompose.theme.ChatScreenMessageBoxPaddingVertical
import com.example.tagginginchat.ui.jetpackCompose.theme.MentionedUserTextColor
import com.example.tagginginchat.ui.jetpackCompose.theme.ReceivedMessageBackground
import com.example.tagginginchat.ui.jetpackCompose.theme.SentMessageBackground
import com.example.tagginginchat.ui.jetpackCompose.theme.TaggingInChatTheme

@Composable
fun MessageBox(
    modifier: Modifier = Modifier,
    message: Message,
    users: List<User>,
    prevMentionedUsers: SnapshotStateList<String>,
) {

    val userInformation = users.find { it.id == message.userId }

    if (userInformation != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(if (message.isSent) Alignment.BottomEnd else Alignment.BottomStart)
        ) {
            Row(
                modifier = Modifier.padding(
                    vertical = ChatScreenMessageBoxPaddingVertical,
                    horizontal = ChatScreenMessageBoxPaddingHorizontal
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (message.isSent) Arrangement.End else Arrangement.Start
            ) {
                Image(
                    painter = painterResource(id = userInformation.profileImage),
                    contentDescription = userInformation.name,
                    modifier = modifier
                        .width(ChatScreenMessageBoxImageWidth)
                        .padding(end = ChatScreenMessageBoxImageEndPadding)
                        .align(Alignment.Top)
                )
                Card(
                    modifier = modifier
                        .wrapContentWidth()
                        .align(Alignment.Top)
                ) {
                    Column(
                        modifier = Modifier
                            .background(if (message.isSent) SentMessageBackground else ReceivedMessageBackground)
                            .wrapContentWidth()

                    ) {
                        val annotatedString = buildAnnotatedString {
                            append(message.content)
                            prevMentionedUsers.forEach { user ->
                                val userMatches = "@$user".toRegex().findAll(message.content)
                                userMatches.forEach { matchResult ->
                                    addStyle(
                                        style = SpanStyle(color = MentionedUserTextColor),
                                        start = matchResult.range.first,
                                        end = matchResult.range.last + 1
                                    )
                                }
                            }
                        }
                        Text(
                            text = userInformation.name,
                            color = userInformation.color,
                            modifier = modifier
                                .padding(horizontal = ChatScreenMessageBoxNameHorizontalPadding)
                                .padding(
                                    top = ChatScreenMessageBoxNameTopPadding,
                                    bottom = ChatScreenMessageBoxNameBottomPadding
                                )
                        )
                        Text(
                            text = annotatedString,
                            color = Color.White,
                            modifier = modifier
                                .padding(horizontal = ChatScreenMessageBoxContentHorizontalPadding)
                                .padding(
                                    bottom = ChatScreenMessageBoxContentBottomPadding,
                                    top = ChatScreenMessageBoxContentTopPadding
                                )
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingMessageBox() {
    TaggingInChatTheme {
        val sent = Message(
            isSent = true,
            userId = 1,
            content = "Hi, how are you?"
        )

        val received = Message(
            isSent = false,
            userId = 2,
            content = "I'm fine, thank you. \n What about you?"
        )

        LazyColumn {
            item {
                MessageBox(
                    modifier = Modifier,
                    message = sent,
                    users = DataSource().users,
                    prevMentionedUsers =  mutableStateListOf()
                )
            }
            item {
                MessageBox(
                    modifier = Modifier,
                    message = received,
                    users = DataSource().users,
                    prevMentionedUsers = mutableStateListOf()
                )
            }
        }
    }
}