package com.example.yourlife.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Gerenciador de token JWT usando SharedPreferences
 * Armazena e recupera o token de autenticação localmente
 */
object TokenManager {

    private const val PREF_NAME = "yourlife_prefs"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Salva o token de autenticação
     */
    fun saveToken(context: Context, token: String) {
        getPrefs(context).edit().putString(KEY_TOKEN, token).apply()
    }

    /**
     * Recupera o token salvo
     */
    fun getToken(context: Context): String? {
        return getPrefs(context).getString(KEY_TOKEN, null)
    }

    /**
     * Verifica se existe um token salvo
     */
    fun hasToken(context: Context): Boolean {
        return !getToken(context).isNullOrEmpty()
    }

    /**
     * Salva informações básicas do usuário
     */
    fun saveUserInfo(context: Context, userId: Int, name: String, email: String) {
        getPrefs(context).edit().apply {
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_EMAIL, email)
            apply()
        }
    }

    /**
     * Recupera o ID do usuário
     */
    fun getUserId(context: Context): Int {
        return getPrefs(context).getInt(KEY_USER_ID, -1)
    }

    /**
     * Recupera o nome do usuário
     */
    fun getUserName(context: Context): String? {
        return getPrefs(context).getString(KEY_USER_NAME, null)
    }

    /**
     * Limpa todas as informações salvas (logout)
     */
    fun clearAll(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}

