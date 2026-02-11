package com.sauban.securemessenger.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sauban.securemessenger.components.ConversationList
import com.sauban.securemessenger.data.conversations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Secure Messenger") }
            )
        },
    ) { paddingValues ->
            ConversationList(conversations = conversations, paddingValues)
    }
}
