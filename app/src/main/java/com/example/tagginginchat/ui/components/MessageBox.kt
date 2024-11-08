package com.example.tagginginchat.ui.components

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
import com.example.tagginginchat.ui.theme.ChatScreenMessageBoxContentBottomPadding
import com.example.tagginginchat.ui.theme.ChatScreenMessageBoxContentHorizontalPadding
import com.example.tagginginchat.ui.theme.ChatScreenMessageBoxContentTopPadding
import com.example.tagginginchat.ui.theme.ChatScreenMessageBoxImageEndPadding
import com.example.tagginginchat.ui.theme.ChatScreenMessageBoxImageWidth
import com.example.tagginginchat.ui.theme.ChatScreenMessageBoxNameBottomPadding
import com.example.tagginginchat.ui.theme.ChatScreenMessageBoxNameHorizontalPadding
import com.example.tagginginchat.ui.theme.ChatScreenMessageBoxNameTopPadding
import com.example.tagginginchat.ui.theme.ChatScreenMessageBoxPaddingHorizontal
import com.example.tagginginchat.ui.theme.ChatScreenMessageBoxPaddingVertical
import com.example.tagginginchat.ui.theme.MentionedUserTextColor
import com.example.tagginginchat.ui.theme.ReceivedMessageBackground
import com.example.tagginginchat.ui.theme.SentMessageBackground
import com.example.tagginginchat.ui.theme.TaggingInChatTheme

@Composable
fun MessageBox(modifier: Modifier = Modifier, message: Message, users: List<User>) {

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
                            val tagMatches = "@[\\w.]+(?:\\s[\\w.]+)*".toRegex().findAll(message.content)
                            tagMatches.forEach { matchResult ->
                                addStyle(
                                    style = SpanStyle(color = MentionedUserTextColor),
                                    start = matchResult.range.first,
                                    end = matchResult.range.last + 1
                                )
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
                    users = DataSource().users
                )
            }
            item {
                MessageBox(
                    modifier = Modifier,
                    message = received,
                    users = DataSource().users
                )
            }
        }
    }
}