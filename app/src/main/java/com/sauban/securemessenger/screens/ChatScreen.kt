package com.sauban.securemessenger.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sauban.securemessenger.helper.CryptoManager
import com.sauban.securemessenger.helper.UserSession
import com.sauban.securemessenger.helper.getToken
import com.sauban.securemessenger.model.ChatMessage
import com.sauban.securemessenger.model.ChatViewModel
import com.sauban.securemessenger.model.Conversation
import com.sauban.securemessenger.network.SocketManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(conversationId: String, viewModel: ChatViewModel = viewModel()) {

    var inputText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatMessage>() }
    val context = LocalContext.current
    UserSession.loadUser(context)
    val conversation by viewModel.conversation.collectAsState()

    LaunchedEffect(conversationId) {
        viewModel.loadConversation(conversationId)
    }

    if (conversation == null) {
        CircularProgressIndicator()
        return
    }

    var sessionKey by remember { mutableStateOf<ByteArray?>(null) }

    LaunchedEffect(conversation) {

        // 1️⃣ Generate identity key once

        // 2️⃣ Connect socket


        // 3️⃣ Join room
//        SocketManager.joinRoom(roomId)

        // 4️⃣ Register public key


        // 5️⃣ Fetch peer public key and derive session key

        if (conversation == null) return@LaunchedEffect
        SocketManager.joinRoom(conversation!!.id)

        val peerUserId = conversation!!.participants
            .firstOrNull { it.user.id != UserSession.userId }
            ?.user?.id ?: return@LaunchedEffect

        SocketManager.getPublicKey(peerUserId) { peerKey ->
            if (peerKey == null) {
                Log.e("E2EE", "Public key not found")
                return@getPublicKey
            }

            try {
                val sharedSecret =
                    CryptoManager.deriveSharedSecret(peerKey)

                sessionKey =
                    CryptoManager.hkdf(sharedSecret)

            } catch (e: Exception) {
                Log.e("E2EE", "Key derivation failed", e)
            }
        }

        // 6️⃣ Listen for new messages
//        SocketManager.onNewMessage { payload ->
//
//            sessionKey?.let { key ->
//
//                val decrypted =
//                    CryptoManager.decryptMessage(
//                        key,
//                        payload.msg,
//                        payload.iv
//                    )
//
//                messages.add(
//                    ChatMessage(decrypted, isMine = false)
//                )
//            }
//        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Person B") })
        },
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {

            LazyColumn(
                modifier = Modifier.weight(1f),
                reverseLayout = true,
            ) {
                items(messages.reversed()) { message ->
                    Text(
                        text = message.message,
                        modifier = Modifier.padding(4.dp),
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {

                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (inputText.isNotBlank() && sessionKey != null) {

                            val (encryptedMsg, iv) =
                                CryptoManager.encryptMessage(
                                    sessionKey!!,
                                    inputText,
                                )

                            SocketManager.sendMessage(
                                conversationId,
                                encryptedMsg,
                                iv,
                            )

                            messages.add(
                                ChatMessage(inputText, isMine = true),
                            )

                            inputText = ""
                        }
                    },
                ) {
                    Text("Send")
                }
            }
        }
    }
}
