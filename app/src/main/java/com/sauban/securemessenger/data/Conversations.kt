package com.sauban.securemessenger.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sauban.securemessenger.helper.User
import com.sauban.securemessenger.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class  Conversation(
    val participants: List<Participants>,
    val name: String?,
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val isGroup: Boolean,
)

class ChatViewModel : ViewModel() {

    private val _conversation = MutableStateFlow<Conversation?>(null)
    val conversation: StateFlow<Conversation?> = _conversation

    fun loadConversation(conversationId: String) {
        viewModelScope.launch {
            val response =
                ApiClient.apiService.getConversation(conversationId)

            _conversation.value = response.conversation
        }
    }
}

data class Participants(
    val id: String,
    val userId : String,
    val conversationId: String,
    val role: String,
    val user: User
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
