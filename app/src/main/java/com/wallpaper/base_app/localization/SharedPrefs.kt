package com.wallpaper.base_app.localization

import android.content.Context
import android.content.SharedPreferences

object SharedPrefs {
    lateinit var spd: SharedPreferences
    const val languageKey = "selected_language"
    const val firstTimeKey = "FirstTime"
    const val firstTimeNotifiKey = "FirstNotifiTime"
    const val firstTimeADKey = "ADshownFirstTime"
    const val defaultLang = "en"

    const val boardingShownKey = "boarding"
    const val localizationShownKey = "localization"
    const val connectTOPCKey = "connectTOPCKEY"
    const val rewardTimeShownKey = "rewardTimeShownKey"
    const val remainingTimeKey = "end_time"

    fun setPrefsString(context: Context, value: String, key: String) {
        getSpf(context).edit().putString(key, value).apply()
    }
    fun setPrefsInt(context: Context, value: Int, key: String) {
        getSpf(context).edit().putInt(key, value).apply()
    }
    fun setPrefsLong(context: Context, value: Long, key: String) {
        getSpf(context).edit().putLong(key, value).apply()
    }

    fun setPrefsBoolean(context: Context, value: Boolean, key: String) {
        getSpf(context).edit().putBoolean(key, value).apply()
    }

    private fun getSpf(context: Context): SharedPreferences {
        if (!this::spd.isInitialized) {
            spd = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        }
        return spd
    }

    fun getPrefsString(context: Context, key: String, defValue: String): String {
        return getSpf(context).getString(key, defValue) ?: defValue
    }

    fun getPrefsInt(context: Context, key: String, defValue: Int): Int {
        return getSpf(context).getInt(key, defValue) ?: defValue
    }

    fun getPrefsLong(context: Context, key: String, defValue: Long): Long {
        return getSpf(context).getLong(key, defValue) ?: defValue
    }

    fun getPrefsBoolean(context: Context, key: String, defValue: Boolean): Boolean {
        return getSpf(context).getBoolean(key, defValue)
    }
}