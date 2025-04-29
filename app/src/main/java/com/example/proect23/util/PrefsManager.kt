package com.example.proect23.util

import android.content.Context
import android.content.SharedPreferences

object PrefsManager {
    private const val PREFS_NAME = "app_prefs"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_REFRESH_TOKEN = "refresh_token"
    private const val KEY_TOKEN = "key_token" // старый ключ

    private lateinit var prefs: SharedPreferences

    /** Инициализировать в Application.onCreate() */
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /** Access-токен (может истекать) */
    var accessToken: String?
        get() = prefs.getString(KEY_ACCESS_TOKEN, null)
        set(value) {
            prefs.edit().putString(KEY_ACCESS_TOKEN, value).apply()
            prefs.edit().putString(KEY_TOKEN, value).apply() // для совместимости
        }

    /** Refresh-токен (для получения нового access) */
    var refreshToken: String?
        get() = prefs.getString(KEY_REFRESH_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_REFRESH_TOKEN, value).apply()

    /** Старый getter, оставлен для совместимости */
    var token: String?
        get() = accessToken
        set(value) {
            accessToken = value
        }

    /** Сбросить все данные (при полном logout) */
    fun clear() {
        prefs.edit().clear().apply()
    }

    /** ✅ Новый метод: удалить только токены без очистки всех настроек */
    fun clearTokens() {
        prefs.edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .remove(KEY_TOKEN)
            .apply()
    }

    fun fetchAccessToken(): String? = accessToken
    fun fetchRefreshToken(): String? = refreshToken
    fun saveAccessToken(token: String?) { accessToken = token }
    fun saveRefreshToken(token: String?) { refreshToken = token }
}
