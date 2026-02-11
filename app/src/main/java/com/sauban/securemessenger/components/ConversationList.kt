package com.sauban.securemessenger.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.sauban.securemessenger.data.Conversation

@Composable
fun ConversationList(conversations: List<Conversation>, paddingValues: PaddingValues) {
    val sorted = remember(conversations) {
        conversations.sortedByDescending { it.lastMessageTime }
    }

    LazyColumn(
        contentPadding = paddingValues
    ) {
        items(
            items = sorted,
            key = { it.id } // prevents recomposition issues
        ) { conversation ->
            ConversationItem(conversation = conversation)
        }
    }
}
