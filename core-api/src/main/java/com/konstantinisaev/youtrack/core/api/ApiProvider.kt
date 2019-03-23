@file:Suppress("DEPRECATION")

package com.konstantinisaev.youtrack.core.api

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiProvider {

    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit
    private lateinit var httpRepository: HttpRepository
    private lateinit var credentialsInterceptor: ServerCredentialsInterceptor

    fun init(apiUrl: String, interceptors: Array<Interceptor>) {
        okHttpClient = initOkHttp()
        retrofit = Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(okHttpClient)
                .build()
        httpRepository = retrofit.create(HttpRepository::class.java)
    }

    private fun initOkHttp(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        credentialsInterceptor = ServerCredentialsInterceptor()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val interceptors = listOf(credentialsInterceptor,loggingInterceptor,JsonInterceptor())

        for (interceptor in interceptors) {
            builder.addInterceptor(interceptor)
        }

        val commonTimeOut = 15L
        builder.connectTimeout(commonTimeOut,TimeUnit.SECONDS)
        builder.readTimeout(commonTimeOut,TimeUnit.SECONDS)
        builder.writeTimeout(commonTimeOut,TimeUnit.SECONDS)
        return builder.build()
    }

    fun isInitialized(): Boolean {
        return this::okHttpClient.isInitialized
    }

    fun enableAppCredentialsInHeader(clientId: String, clientSecret: String){
        credentialsInterceptor.initServiceCredentials(clientId,clientSecret)
    }

    fun enableUserCredentialsInHeader(accessToken: String,tokenType: String){
        credentialsInterceptor.initUserCredentials(accessToken,tokenType)
    }

    fun getServerVersion(url: String) = httpRepository.getServerVersion(url)

    fun getServerConfig(url: String) = httpRepository.getServerConfig(url)

//    fun login(url: String,login: String,password: String,scope: String) = httpRepository.login(url,"offline","password",login,password,scope)
//
//    fun refreshToken(url: String,token: String) = httpRepository.refreshToken(url,"refresh_token",token)
//
//    fun getProfile(baseUrl: String) = httpRepository.getCurrentUser("$baseUrl${ApiEndpoints.GET_CURRENT_USER.url}")
//
//    fun getProjects(baseUrl: String) = httpRepository.getProjects("$baseUrl${ApiEndpoints.GET_PROJECTS.url}")
//
//    fun getAllIssues(baseUrl: String, query: String? = null, top: Int = DEFAULT_ISSUE_LIST_SIZE, skip: Int = 0) = httpRepository.getAllIssues("$baseUrl${ApiEndpoints.GET_ALL_ISSUES.url}",query = query,top =  top,skip = skip)
//
//    fun getIssuesFilterIntellisense(baseUrl: String, query: String? = null, caret: Int? = null, optionsLimit: Int = DEFAULT_ISSUE_LIST_SIZE) =
//        httpRepository.getIssuesFilterSuggestions("$baseUrl${ApiEndpoints.GET_ISSUE_FILTER_INTELLISENCE.url}",query,caret,optionsLimit)
//
//    fun getAllIssuesCount(baseUrl: String, filter: String? = null) = httpRepository.getAllIssuesCount("$baseUrl${ApiEndpoints.GET_ALL_ISSUES_COUNT.url}",filter)
//
//    fun createIssue(baseUrl: String, createIssueDTO: CreateIssueDTO) = httpRepository.createIssue("$baseUrl${ApiEndpoints.GET_ALL_ISSUES.url}",createIssueDTO)
//
//    fun getCustomFieldSettings(baseUrl: String,parentType: String,parentId: String) = httpRepository.getCustomFieldBundle("$baseUrl${String.format(ApiEndpoints.GET_CUSTOM_FIELD_SETTING.url,parentType,parentId)}")
//
//    fun initDraft(baseUrl: String,projectId: String) =
//        httpRepository.initDraft("$baseUrl${String.format(ApiEndpoints.DRAFT.url,"")}",createIssueDTO = DefaultCreateIssueBodyDTO(project = CreateIssueProjectDTO(id = projectId)))
//
//    fun getIssueByDraftId(baseUrl: String, lastDraftId: String) = httpRepository.getIssueByDraftId("$baseUrl${String.format(ApiEndpoints.DRAFT.url,lastDraftId)}")
//
//    fun getCustomFieldUserSettings(baseUrl: String,parentType: String,parentId: String) = httpRepository.getCustomFieldUsers("$baseUrl${String.format(ApiEndpoints.GET_USER_CUSTOM_FIELD_SETTING.url,parentType,parentId)}")
//
//    fun updateDraft(baseUrl: String,lastDraftId: String,valueId: String,value: Any) = httpRepository.updateDraft("$baseUrl${String.format(ApiEndpoints.UPDATE_FIELD.url,lastDraftId,valueId)}",updateDraftDTO = UpdateDraftDTO(valueId,value))

}

