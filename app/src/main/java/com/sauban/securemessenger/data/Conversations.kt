package com.sauban.securemessenger.model

data class Conversation(
    val id: Int,
    val name: String,
    val lastMessage: String,
    val lastMessageTime: Long // epoch millis
)

val conversations = List(50) { index ->
    Conversation(
        id = index,
        name = "Contact ${index + 1}",
        lastMessage = "Last message from Contact ${index + 1}",
        lastMessageTime = System.currentTimeMillis() - (index * 60 * 1000L)
    )
}

data class ChatMessage(
    val message: String,
    val isMine: Boolean
)
data class EncryptedPayload(
    val msg: String,
    val iv: String,
    val senderId: String
)
