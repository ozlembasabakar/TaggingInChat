package com.example.tagginginchat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.example.tagginginchat.data.DataSource
import com.example.tagginginchat.data.model.User
import com.example.tagginginchat.ui.theme.ChatScreenUserListCornerRadius
import com.example.tagginginchat.ui.theme.ChatScreenUserListItemSize
import com.example.tagginginchat.ui.theme.ChatScreenUserListStartPadding
import com.example.tagginginchat.ui.theme.TagLayoutBackground
import com.example.tagginginchat.ui.theme.TaggingInChatTheme

@Composable
fun TagLayout(
    modifier: Modifier = Modifier,
    users: List<User>,
    searchedText: String,
    onUserSelected: (User) -> Unit,
) {

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(BottomStart)
            .padding(start = ChatScreenUserListStartPadding)
            .clip(
                RoundedCornerShape(
                    topStart = ChatScreenUserListCornerRadius,
                    topEnd = ChatScreenUserListCornerRadius
                )
            )
            .background(TagLayoutBackground)
            .height(ChatScreenUserListItemSize * 5)
    ) {

        items(users) { user ->
            UserLayout(
                user = user, searchedText = searchedText
            ) {
                onUserSelected(it)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTagLayout() {
    TaggingInChatTheme {
        TagLayout(Modifier, DataSource().users, "Spon", onUserSelected = {})
    }
}