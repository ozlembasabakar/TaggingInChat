package com.example.tagginginchat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.example.tagginginchat.ui.theme.ChatScreenUserListItemDividerPadding
import com.example.tagginginchat.ui.theme.ChatScreenUserListItemDividerThickness
import com.example.tagginginchat.ui.theme.ChatScreenUserListItemHorizontalPadding
import com.example.tagginginchat.ui.theme.ChatScreenUserListItemImageWidth
import com.example.tagginginchat.ui.theme.ChatScreenUserListItemTextBottomPadding
import com.example.tagginginchat.ui.theme.ChatScreenUserListItemTextHeight
import com.example.tagginginchat.ui.theme.ChatScreenUserListItemTextHorizontalPadding
import com.example.tagginginchat.ui.theme.ChatScreenUserListItemTextTopPadding
import com.example.tagginginchat.ui.theme.ChatScreenUserListItemVerticalPadding
import com.example.tagginginchat.ui.theme.ChatScreenUserListItemWidthDifference
import com.example.tagginginchat.ui.theme.DividerColor
import com.example.tagginginchat.ui.theme.TagLayoutBackground
import com.example.tagginginchat.ui.theme.TaggingInChatTheme


@Composable
fun UserLayout(
    modifier: Modifier = Modifier,
    user: User,
    searchedText: String,
    onUserSelected: (User) -> Unit,
) {

    Column(
        modifier = modifier
            .width((LocalConfiguration.current.screenWidthDp - ChatScreenUserListItemWidthDifference).dp)
            .clickable { onUserSelected(user) },
    ) {
        Row(
            modifier = Modifier
                .padding(
                    vertical = ChatScreenUserListItemVerticalPadding,
                    horizontal = ChatScreenUserListItemHorizontalPadding
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = user.profileImage),
                contentDescription = user.name,
                modifier = modifier
                    .width(ChatScreenUserListItemImageWidth)
                    .align(Alignment.CenterVertically)
                    .clip(CircleShape)
            )

            Text(
                text = getStyledText(
                    fullName = user.name,
                    searchText = searchedText
                ),
                color = Color.White,
                modifier = modifier
                    .padding(horizontal = ChatScreenUserListItemTextHorizontalPadding)
                    .padding(
                        top = ChatScreenUserListItemTextTopPadding,
                        bottom = ChatScreenUserListItemTextBottomPadding
                    )
                    .height(ChatScreenUserListItemTextHeight)
            )
        }
        HorizontalDivider(
            thickness = ChatScreenUserListItemDividerThickness,
            color = DividerColor,
            modifier = Modifier
                .padding(start = ChatScreenUserListItemDividerPadding)
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
fun PreviewUserLayout() {
    TaggingInChatTheme {
        UserLayout(
            Modifier.background(TagLayoutBackground), DataSource().users[0], "Spon"
        ) {}
    }
}