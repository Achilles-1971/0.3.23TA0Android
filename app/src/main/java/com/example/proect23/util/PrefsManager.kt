package com.example.proect23.util

import android.content.Context
import android.content.SharedPreferences

object PrefsManager {
    private const val PREFS_NAME = "app_prefs"
    private const val KEY_TOKEN = "key_token"
    private lateinit var prefs: SharedPreferences

    /** Инициализировать в Application.onCreate() */
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /** Текущий токен (или null, если не залогинен) */
    var token: String?
        get() = prefs.getString(KEY_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_TOKEN, value).apply()

    /** Сбросить все данные (при logout) */
    fun clear() {
        prefs.edit().clear().apply()
    }
}
