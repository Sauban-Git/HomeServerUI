package com.sauban.securemessenger.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.sauban.securemessenger.helper.User
import com.sauban.securemessenger.model.Conversation

@Composable
fun ConversationList(conversations: List<Conversation>, paddingValues: PaddingValues) {
//    val sorted = remember(conversations) {
//        conversations.sortedByDescending { it.lastMessageTime }
//    }

    LazyColumn(
        contentPadding = paddingValues
    ) {
        items(
            items = conversations,
            key = { it.id } // prevents recomposition issues
        ) { conversation ->
            ConversationItem(conversation = conversation)
        }
    }
}

@Composable
fun UsersList(users: List<User>, paddingValues: PaddingValues) {
    LazyColumn(
        contentPadding = paddingValues
    ) {
        items(
            items = users,
            key = { it.id } // prevents recomposition issues
        ) { user ->
            UsersItem(user = user)
        }
    }
}
