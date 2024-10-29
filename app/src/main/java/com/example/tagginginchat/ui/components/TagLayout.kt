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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tagginginchat.data.DataSource
import com.example.tagginginchat.data.model.User
import com.example.tagginginchat.ui.theme.DividerColor
import com.example.tagginginchat.ui.theme.TagLayoutBackground
import com.example.tagginginchat.ui.theme.TaggingInChatTheme

@Composable
fun TagLayout(modifier: Modifier = Modifier, users: List<User>, searchedText: String) {

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(BottomStart)
            .padding(start = 2.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(TagLayoutBackground)
            .height(53.dp * 5)
    ) {

        items(users) { user ->
            UserLayout(
                user = user, searchedText = searchedText
            )
        }
    }
}

@Composable
fun UserLayout(modifier: Modifier = Modifier, user: User, searchedText: String) {

    Column(
        modifier = modifier
            .width((LocalConfiguration.current.screenWidthDp - 54).dp),
    ) {
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
                text = getStyledText(
                    fullName = "${user.name} ${user.surname}",
                    searchText = searchedText
                ),
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

@Composable
fun getStyledText(fullName: String, searchText: String): AnnotatedString {
    return buildAnnotatedString {
        val startIndex = fullName.indexOf(searchText, ignoreCase = true)
        if (startIndex != -1 && searchText.isNotEmpty()) {
            append(fullName.substring(0, startIndex))
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(fullName.substring(startIndex, startIndex + searchText.length))
            }
            append(fullName.substring(startIndex + searchText.length))
        } else {
            append(fullName)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTagLayout() {
    TaggingInChatTheme {
        TagLayout(Modifier, DataSource().users, "Spon")
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewUserLayout() {
    TaggingInChatTheme {
        UserLayout(
            Modifier.background(TagLayoutBackground), DataSource().users[0], "Spon"
        )
    }
}