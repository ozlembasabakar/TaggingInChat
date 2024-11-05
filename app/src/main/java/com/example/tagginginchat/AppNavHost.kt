package com.example.tagginginchat

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tagginginchat.ui.ChatScreen
import com.example.tagginginchat.ui.ChatScreenViewModel

@Composable
fun AppNavHost() {

    val navController = rememberNavController()

    val chatScreenViewModel: ChatScreenViewModel = hiltViewModel()
    val viewState by chatScreenViewModel.state.collectAsStateWithLifecycle()

    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = Screen.ChatScreen.route
    ) {
        composable(Screen.ChatScreen.route) {

            val scrollState = rememberLazyListState()

            LaunchedEffect(viewState.messageList.size) {
                if (viewState.messageList.isNotEmpty()) {
                    scrollState.animateScrollToItem(viewState.messageList.size - 1)
                }
            }

            ChatScreen(
                scrollState = scrollState,
                viewState = viewState,
                chatScreenInputModel = chatScreenViewModel.chatScreenInputModel,
                sendMessage = chatScreenViewModel::sendMessage,
                receivedMessage = chatScreenViewModel::receivedMessage
            )
        }
    }
}

sealed class Screen(
    val route: String,
) {
    data object ChatScreen : Screen("ChatScreen")
}