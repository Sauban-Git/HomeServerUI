package com.sauban.securemessenger.helper

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.*
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec
import android.util.Base64
import java.security.spec.ECGenParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.spec.GCMParameterSpec

object CryptoManager {

    private const val KEY_ALIAS = "identity_key"

    fun generateIdentityKeyIfNeeded() {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        if (keyStore.containsAlias(KEY_ALIAS)) return

        val kpg = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_EC,
            "AndroidKeyStore"
        )

        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_AGREE_KEY
        )
            .setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
            .build()

        kpg.initialize(spec)
        kpg.generateKeyPair()
    }

    fun getPublicKeyBase64(): String {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        val publicKey = keyStore.getCertificate(KEY_ALIAS).publicKey
        return Base64.encodeToString(publicKey.encoded, Base64.NO_WRAP)
    }

    fun deriveSharedSecret(peerPublicKeyBase64: String?): ByteArray {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        val privateKey = keyStore.getKey(KEY_ALIAS, null) as PrivateKey

        val keyFactory = KeyFactory.getInstance("EC")
        val peerKeyBytes = Base64.decode(peerPublicKeyBase64, Base64.NO_WRAP)
        val peerPublicKey = keyFactory.generatePublic(X509EncodedKeySpec(peerKeyBytes))

        val keyAgreement = KeyAgreement.getInstance("ECDH")
        keyAgreement.init(privateKey)
        keyAgreement.doPhase(peerPublicKey, true)

        return keyAgreement.generateSecret()
    }
    fun hkdf(inputKeyMaterial: ByteArray): ByteArray {
        val mac = Mac.getInstance("HmacSHA256")
        val salt = ByteArray(32) { 0 }
        val keySpec = SecretKeySpec(salt, "HmacSHA256")
        mac.init(keySpec)
        return mac.doFinal(inputKeyMaterial).copyOf(32)
    }

    fun encryptMessage(sessionKey: ByteArray, message: String): Pair<String, String> {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")

        val secretKey = SecretKeySpec(sessionKey, "AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val iv = cipher.iv
        val encrypted = cipher.doFinal(message.toByteArray())

        return Pair(
            Base64.encodeToString(encrypted, Base64.NO_WRAP),
            Base64.encodeToString(iv, Base64.NO_WRAP)
        )
    }

    fun decryptMessage(sessionKey: ByteArray, msg: String, iv: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")

        val secretKey = SecretKeySpec(sessionKey, "AES")
        cipher.init(
            Cipher.DECRYPT_MODE,
            secretKey,
            GCMParameterSpec(128, Base64.decode(iv, Base64.NO_WRAP))
        )

        val decrypted = cipher.doFinal(Base64.decode(msg, Base64.NO_WRAP))
        return String(decrypted)
    }
}


