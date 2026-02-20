package com.sauban.securemessenger.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.sauban.securemessenger.helper.User
import com.sauban.securemessenger.model.Conversation

@Composable
fun ConversationList(
    navController: NavController,
    conversations: List<Conversation>,
    paddingValues: PaddingValues,
    snackBarHostState: SnackbarHostState,
) {
//    val sorted = remember(conversations) {
//        conversations.sortedByDescending { it.lastMessageTime }
//    }

    LazyColumn(
        contentPadding = paddingValues,

        ) {
        items(
            items = conversations,
            key = { it.id }, // prevents recomposition issues
        ) { conversation ->
            ConversationItem(
                navController = navController,
                conversation = conversation,
                snackBarHostState = snackBarHostState,
            )
        }
    }
}

@Composable
fun UsersList(users: List<User>, paddingValues: PaddingValues, snackBarHostState: SnackbarHostState) {
    LazyColumn(
        contentPadding = paddingValues,
    ) {
        items(
            items = users,
            key = { it.id }, // prevents recomposition issues
        ) { user ->
            UsersItem(user = user, snackBarHostState)
        }
    }
}
