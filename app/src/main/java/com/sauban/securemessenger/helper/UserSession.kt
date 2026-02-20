package com.sauban.securemessenger.helper

import android.content.Context
import androidx.core.content.edit

object UserSession {
    var userId: String? = null
    var userName: String? = null
    var userPhone: String? = null

    private const val PREFS_NAME = "user_prefs"
    private const val KEY_ID = "user_id"
    private const val KEY_NAME = "user_name"
    private const val KEY_PHONE = "user_phone"

    fun saveUser(context: Context, id: String, name: String, phone: String) {
        userId = id
        userName = name
        userPhone = phone

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putString(KEY_ID, id)
            putString(KEY_NAME, name)
            putString(KEY_PHONE, phone)
        }
    }

    fun loadUser(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        userId = prefs.getString(KEY_ID, null)
        userName = prefs.getString(KEY_NAME, null)
        userPhone = prefs.getString(KEY_PHONE, null)
    }

    fun clearUser(context: Context) {
        userId = null
        userName = null
        userPhone = null
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { clear() }
    }
}
