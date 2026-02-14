package com.sauban.securemessenger.network

import android.util.Log
import com.sauban.securemessenger.model.EncryptedPayload
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.channels.Channel
import org.json.JSONObject
import java.net.URISyntaxException
import io.socket.client.Ack

object SocketManager {

    private var mSocket: Socket? = null
    private val newMessageChannel = Channel<String>(Channel.UNLIMITED)

    fun init(token: String) {
        try {
            val opts = IO.Options().apply {
                query = "" // optional
                forceNew = true
                reconnection = true
                extraHeaders = mapOf(
                    "Authorization" to listOf("Bearer $token"),
                )
            }

            mSocket = IO.socket("https://securefamily.duckdns.org:3786", opts)

            mSocket?.on(Socket.EVENT_CONNECT) {
                Log.d("Socket", "Connected as something")
            }

            mSocket?.on(Socket.EVENT_DISCONNECT) {
                Log.d("Socket", "Disconnected")
            }

            mSocket?.connect()

        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    // Join a conversation room
    fun joinRoom(roomId: String) {
        val payload = JSONObject().apply {
            put("roomId", roomId)
        }

        mSocket?.emit("conversation:join", payload)
    }

    // Send a new encrypted message
    fun sendMessage(roomId: String, msg: String, iv: String) {
        val payload = mutableMapOf(
            "roomId" to roomId,
            "msg" to msg,
            "iv" to iv
        )

        mSocket?.emit("message:new", payload)
    }

    // Listen for incoming messages
    fun onNewMessage(callback: (EncryptedPayload) -> Unit) {
        mSocket?.on("message:new") { args ->
            val data = args[0] as JSONObject

            callback(
                EncryptedPayload(
                    msg = data.getString("msg"),
                    iv = data.getString("iv"),
                    senderId = data.getString("senderId")
                ),
            )
        }
    }


    fun registerPublicKey(publicKey: String) {
        val payload = JSONObject()
        payload.put("publicKey", publicKey)

        mSocket?.emit("e2ee:register-key", payload)
    }
    fun getPublicKey(userId: String, callback: (String?) -> Unit) {
        val payload = JSONObject().put("userId", userId)

        mSocket?.emit("e2ee:get-key", payload, Ack { args ->

            val key = args.getOrNull(0) as? String
            callback(key)

        })
    }

    fun disconnect() {
        mSocket?.disconnect()
        mSocket = null
    }
}
