package com.example.tagginginchat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tagginginchat.data.DataSource
import com.example.tagginginchat.data.model.User
import com.example.tagginginchat.ui.theme.DividerColor
import com.example.tagginginchat.ui.theme.TagLayoutBackground
import com.example.tagginginchat.ui.theme.TaggingInChatTheme

@Composable
fun TagLayout(modifier: Modifier = Modifier, users: List<User>) {

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(BottomStart)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(TagLayoutBackground)
            .height(53.dp * 5)
    ) {

        items(users) { user ->
            UserLayout(
                user = user
            )
        }
    }
}

@Composable
fun UserLayout(modifier: Modifier = Modifier, user: User) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = user.profileImage),
                contentDescription = user.name,
                modifier = modifier
                    .width(24.dp)
                    .align(Alignment.CenterVertically)
                    .clip(CircleShape)
            )

            Text(
                text = user.name + " " + user.surname,
                color = Color.White,
                modifier = modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp, bottom = 4.dp)
                    .height(24.dp)
            )
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = DividerColor,
            modifier = Modifier
                .padding(start = 48.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewTagLayout() {
    TaggingInChatTheme {
        TagLayout(Modifier, DataSource().users)
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewUserLayout() {
    TaggingInChatTheme {
        UserLayout(
            Modifier.background(TagLayoutBackground), DataSource().users[0]
        )
    }
}