package com.sauban.securemessenger.model

data class  Conversation(
    val participants: List<Participants>,
    val name: String,
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val isGroup: Boolean,
)

data class Participants(
    val id: String,
    val userId : String,
    val conversationId: String,
    val role: String,
)

data class ChatMessage(
    val message: String,
    val isMine: Boolean
)
data class EncryptedPayload(
    val msg: String,
    val iv: String,
    val senderId: String
)
