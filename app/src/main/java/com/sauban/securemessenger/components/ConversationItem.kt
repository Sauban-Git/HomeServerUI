package com.sauban.securemessenger.components

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sauban.securemessenger.helper.User
import com.sauban.securemessenger.helper.UserSession
import com.sauban.securemessenger.model.Conversation
import com.sauban.securemessenger.network.SocketManager
import kotlinx.coroutines.launch

@Composable
fun ConversationItem(conversation: Conversation, snackBarHostState: SnackbarHostState) {
//    val timeText = remember(conversation.lastMessageTime) {
//        formatTime(conversation.lastMessageTime)
//    }
    val context = LocalContext.current
    UserSession.loadUser(context)

    val userId = UserSession.userId
    val name = UserSession.userName
    val phone = UserSession.userPhone
        val scope = rememberCoroutineScope()
    val displayName = conversation.name ?: conversation.participants
        .firstOrNull { it.user.id != userId }?.let { it.user.name ?: "Unknown" }
    ?: "Unknown"
    Row(
        modifier = Modifier
            .clickable {
                // Show snackBar with conversation ID
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = "Conversation ID: ${conversation.id}"
                    )
                }
            }
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp) // rounded corners
            )
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "No msg yet",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

//        Text(
//            text = timeText,
//            style = MaterialTheme.typography.bodySmall,
//            color = MaterialTheme.colorScheme.onSurface
//        )
    }
}

@Composable
fun UsersItem(user: User, snackBarHostState: SnackbarHostState) {
//    val timeText = remember(conversation.lastMessageTime) {
//        formatTime(conversation.lastMessageTime)
//    }

    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .clickable {
                SocketManager.createConversation(user.id) { response ->
                    response?.let {
                        println(response)
                        val conversation = it.getJSONObject("conversation")

                        val id = conversation.getString("id")
                        val name = conversation.getString("name")
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = "Conversation created ID: $id"
                            )
                        }
                    }
                }
                // Show snackBar with conversation ID
      /*          scope.launch {
                    snackBarHostState.showSnackbar(
                        message = "Conversation ID: ${user.id}"
                    )
                }*/
            }
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user.phoneNumber,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

//        Text(
//            text = timeText,
//            style = MaterialTheme.typography.bodySmall,
//            color = MaterialTheme.colorScheme.onSurface
//        )
    }
}
