package com.sauban.securemessenger.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sauban.securemessenger.helper.CryptoManager
import com.sauban.securemessenger.helper.getToken
import com.sauban.securemessenger.model.ChatMessage
import com.sauban.securemessenger.network.SocketManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {

    var inputText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatMessage>() }
    val context = LocalContext.current

    val roomId = "room101"
    val peerUserId = "ca108c28-e468-430c-9776-eb04bcba21b8" // TODO: dynamic later
    val token = getToken(context) ?: ""

    var sessionKey by remember { mutableStateOf<ByteArray?>(null) }

    LaunchedEffect(Unit) {

        // 1️⃣ Generate identity key once
        CryptoManager.generateIdentityKeyIfNeeded()

        // 2️⃣ Connect socket
        SocketManager.init(token)

        // 3️⃣ Join room
        SocketManager.joinRoom(roomId)

        // 4️⃣ Register public key
        SocketManager.registerPublicKey(
            CryptoManager.getPublicKeyBase64()
        )

        // 5️⃣ Fetch peer public key and derive session key
        SocketManager.getPublicKey(peerUserId) { peerKey ->

            val sharedSecret =
                CryptoManager.deriveSharedSecret(peerKey)

            sessionKey =
                CryptoManager.hkdf(sharedSecret)
        }

        // 6️⃣ Listen for new messages
        SocketManager.onNewMessage { payload ->

            sessionKey?.let { key ->

                val decrypted =
                    CryptoManager.decryptMessage(
                        key,
                        payload.msg,
                        payload.iv
                    )

                messages.add(
                    ChatMessage(decrypted, isMine = false)
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Person B") })
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                                    inputText
                                )

                            SocketManager.sendMessage(
                                roomId,
                                encryptedMsg,
                                iv
                            )

                            messages.add(
                                ChatMessage(inputText, isMine = true)
                            )

                            inputText = ""
                        }
                    }
                ) {
                    Text("Send")
                }
            }
        }
    }
}
