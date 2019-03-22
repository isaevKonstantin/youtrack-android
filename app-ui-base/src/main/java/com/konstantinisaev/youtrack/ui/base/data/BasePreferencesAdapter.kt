package com.konstantinisaev.youtrack.ui.base.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.konstantinisaev.youtrack.core.api.ServerConfigDTO

private const val COMMON_PREFERENCE_NAME = "base_preferences"
private const val SERVER_URL = "base_server_url"
private const val SERVER_CONFIG = "base_server_config"

class BasePreferencesAdapter private constructor() {

    companion object {

        lateinit var sharedPreferences: SharedPreferences
        lateinit var gson: Gson

        fun getInstance(context: Context): BasePreferencesAdapter {
            sharedPreferences = context.getSharedPreferences(COMMON_PREFERENCE_NAME, Context.MODE_PRIVATE)
            gson = Gson()
            return BasePreferencesAdapter()
        }
    }


    fun getUrl() : String = sharedPreferences.getString(SERVER_URL,"").orEmpty()
    fun setUrl(serverUrl: String){
        sharedPreferences.edit().putString(SERVER_URL,serverUrl).apply()
    }

    fun getServerConfig(): ServerConfigDTO? {
        val configStr = sharedPreferences.getString(SERVER_CONFIG,"").orEmpty()
        return if(configStr.isNotEmpty()){
            gson.fromJson(configStr,ServerConfigDTO::class.java)
        }else{
            null
        }
    }

    fun setServerConfig(serverConfig: ServerConfigDTO?){
        serverConfig ?: let {
            sharedPreferences.edit().putString(SERVER_CONFIG,"").apply()
            return
        }
        val configStr = gson.toJson(serverConfig)
        sharedPreferences.edit().putString(SERVER_CONFIG,configStr).apply()
    }
}