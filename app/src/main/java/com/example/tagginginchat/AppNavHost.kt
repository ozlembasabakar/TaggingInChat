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
import com.example.tagginginchat.ui.ChatScreenViewModel
import com.example.tagginginchat.ui.MainScreen
import com.example.tagginginchat.ui.jetpackCompose.ChatScreen

@Composable
fun AppNavHost() {

    val navController = rememberNavController()

    val chatScreenViewModel: ChatScreenViewModel = hiltViewModel()
    val viewState by chatScreenViewModel.state.collectAsStateWithLifecycle()

    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = Screen.MainScreen.route
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
                chatScreenViewState = viewState,
                onSelectedUser = chatScreenViewModel::onSelectedUser,
                onMessageChanged = chatScreenViewModel::onMessageChanged,
                sendMessage = chatScreenViewModel::sendMessage,
                receivedMessage = chatScreenViewModel::receivedMessage
            )
        }

        composable(Screen.MainScreen.route) {
            MainScreen(navController = navController)
        }
    }
}

sealed class Screen(
    val route: String,
) {
    data object MainScreen : Screen("MainScreen")
    data object ChatScreen : Screen("ChatScreen")
}