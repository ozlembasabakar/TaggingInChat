package com.example.tagginginchat.ui

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.tagginginchat.Screen
import com.example.tagginginchat.ui.viewBased.ViewBasedMainActivity

@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier,
            onClick = {
                context.startActivity(Intent(context, ViewBasedMainActivity::class.java))
            }
        ) {
            Text("Go to XML Screen")
        }
        Button(
            modifier = Modifier,
            onClick = {
                navController.navigate(Screen.ChatScreen.route)
            }
        ) {
            Text("Go to Jetpack Compose Screen")
        }
    }
}