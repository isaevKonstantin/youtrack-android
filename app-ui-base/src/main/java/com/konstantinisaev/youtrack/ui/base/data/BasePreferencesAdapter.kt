package com.konstantinisaev.youtrack.ui.base.data

import android.content.Context

private const val COMMON_PREFERENCE_NAME = "base_preferences"
private const val SERVER_URL = "base_server_url"

class BasePreferencesAdapter(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(COMMON_PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getUrl() = sharedPreferences.getString(SERVER_URL,"")
    fun setUrl(serverUrl: String){
        sharedPreferences.edit().putString(SERVER_URL,serverUrl).apply()
    }

}