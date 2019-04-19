package com.konstantinisaev.youtrack.ui.base.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.konstantinisaev.youtrack.core.api.AuthTokenDTO
import com.konstantinisaev.youtrack.core.api.ServerConfigDTO

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

    fun getAuthToken(): AuthTokenDTO? {
        val tokenStr = sharedPreferences.getString(TOKEN,"").orEmpty()
        return if(tokenStr.isNotEmpty()){
            gson.fromJson(tokenStr,AuthTokenDTO::class.java)
        }else{
            null
        }

    }

    fun setAuthToken(authToken: AuthTokenDTO?){
        authToken ?: let {
            sharedPreferences.edit().putString(TOKEN,"").apply()
            return
        }
        val tokenStr = gson.toJson(authToken)
        sharedPreferences.edit().putString(TOKEN,tokenStr).apply()
    }

    fun getIssueListType() = sharedPreferences.getInt(ISSUE_LIST_TYPE,0)

    fun setIssueListType(type: Int){
        sharedPreferences.edit().putInt(ISSUE_LIST_TYPE,type).apply()
    }

    fun getSavedQuery() = sharedPreferences.getString(SAVED_QUERY,"").orEmpty()

    fun setSavedQuery(query: String){
        sharedPreferences.edit().putString(SAVED_QUERY,query).apply()
    }

    fun getSortQuery() = sharedPreferences.getString(SORT_QUERY,"").orEmpty()

    fun setSortQuery(query: String){
        sharedPreferences.edit().putString(SORT_QUERY,query).apply()
    }

    fun getLastDraftId() = sharedPreferences.getString(LAST_DRAFT_ID,"").orEmpty()

    fun setLastDraftId(lastDraftId: String){
        sharedPreferences.edit().putString(LAST_DRAFT_ID,lastDraftId).apply()
    }

    fun getCurrentProjectId() = sharedPreferences.getString(CURRENT_PROJECT_ID,"").orEmpty()

    fun setCurrentProjectId(projectId: String){
        sharedPreferences.edit().putString(CURRENT_PROJECT_ID,projectId).apply()
    }
}

private const val COMMON_PREFERENCE_NAME = "base_preferences"
private const val SERVER_URL = "base_server_url"
private const val SERVER_CONFIG = "base_server_config"
private const val TOKEN = "token"
private const val ISSUE_LIST_TYPE = "issue_list_type"
private const val SAVED_QUERY = "saved_query"
private const val SORT_QUERY = "sort_query"
private const val LAST_DRAFT_ID = "last_draft_id"
private const val CURRENT_PROJECT_ID = "current_project_id"