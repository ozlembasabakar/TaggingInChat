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
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tagginginchat.data.DataSource
import com.example.tagginginchat.data.model.Message
import com.example.tagginginchat.data.model.User
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
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (message.isSent) Arrangement.End else Arrangement.Start
            ) {
                Image(
                    painter = painterResource(id = userInformation.profileImage),
                    contentDescription = userInformation.name,
                    modifier = modifier
                        .width(40.dp)
                        .padding(end = 16.dp)
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
                        Text(
                            text = userInformation.name + " " + userInformation.surname,
                            color = userInformation.color,
                            modifier = modifier
                                .padding(horizontal = 8.dp)
                                .padding(top = 8.dp, bottom = 4.dp)
                        )
                        Text(
                            text = message.content,
                            color = Color.White,
                            modifier = modifier
                                .padding(horizontal = 8.dp)
                                .padding(bottom = 8.dp, top = 4.dp)
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
        MessageBox(
            modifier = Modifier,
            message = DataSource().messages[1],
            users = DataSource().users
        )
    }
}