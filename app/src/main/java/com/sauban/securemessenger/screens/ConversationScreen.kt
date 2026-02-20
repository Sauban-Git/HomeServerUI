package com.sauban.securemessenger.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sauban.securemessenger.components.ConversationList
import com.sauban.securemessenger.components.UsersList
import com.sauban.securemessenger.helper.User
import com.sauban.securemessenger.model.Conversation
import com.sauban.securemessenger.network.ApiClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen() {

    var conversations by remember { mutableStateOf<List<Conversation>>(emptyList()) }
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        try {
            val response = ApiClient.apiService.getConversations()
            conversations = response.conversations ?: emptyList()
            val response2 = ApiClient.apiService.getUser()
            users = response2.users ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Secure Messenger") }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(24.dp)) {
            if (conversations.isEmpty()) {
                Text("No conversation yet")
            } else {
                ConversationList(
                    conversations = conversations,
                    paddingValues = PaddingValues(0.dp),
                    snackBarHostState
                )
            }

            if (users.isEmpty()) {
                Text("No users yet")
            } else {
                UsersList(
                    users = users,
                    paddingValues = PaddingValues(0.dp),
                    snackBarHostState
                )
            }
        }

    }
}
