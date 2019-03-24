package com.konstantinisaev.youtrack.core.api

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface HttpRepository {

    @GET("")
    fun getServerVersion(@Url url: String) : Deferred<ServerVersionDTO>

    @GET("")
    fun getServerConfig(@Url url: String,@Query("fields")fields: String? = Fields.SERVER_CONFIG) : Deferred<ServerConfigDTO>


    @POST("")
    fun login(@Url url: String,@Query("access_type") accessType: String, @Query("grant_type") grantType: String,
              @Query("username") userName: String, @Query("password") password: String, @Query("scope") scope: String) : Deferred<AuthTokenDTO>
//
//    @POST("")
//    fun refreshToken(@Url url: String, @Query("grant_type") grantType: String,
//              @Query("refresh_token") token: String) : Deferred<Response<AuthTokenDTO>>
//
//    @GET("")
//    fun getCurrentUser(@Url url: String) : Deferred<Response<CurrentUserDTO>>
//
//    @GET("")
//    fun getProjects(@Url url: String,@Query("fields") fields: String? = Fields.PROJECT) : Deferred<Response<List<ProjectDTO>>>
//
//    @GET("")
//    fun getAllIssues(@Url url: String, @Query("query") query: String? = null,@Query("\$top") top: Int = DEFAULT_ISSUE_LIST_SIZE,@Query("\$skip") skip: Int = 0, @Query("fields") fields: String? = Fields.ISSUE_LIST) : Deferred<Response<List<IssueDTO>>>
//
//    @GET("")
//    fun getIssuesFilterSuggestions(@Url url: String, @Query("query") filter: String? = null, @Query("caret") caret: Int? = null,
//                                   @Query("optionsLimit") optionsLimit: Int? = DEFAULT_ISSUE_LIST_SIZE) : Deferred<Response<RespIssueFilterIntellisense>>
//
//    @GET("")
//    fun getAllIssuesCount(@Url url: String, @Query("filter") filter: String? = null) : Deferred<Response<RespIssueCountDTO>>
//
//    @POST("")
//    fun createIssue(@Url url: String,@Body createIssueDTO: CreateIssueDTO, @Query("fields") fields: String? = Fields.ISSUE_LIST) : Deferred<Response<IssueDTO>>
//
//    @GET("")
//    fun getCustomFieldBundle(@Url url: String,@Query("fields") fields: String? = Fields.CUSTOM_FIELDS) : Deferred<Response<List<CustomFieldAdminDTO>>>
//
//    @GET("")
//    fun getCustomFieldUsers(@Url url: String,@Query("fields") fields: String? = Fields.USER_CUSTOM_FIELD,@Query("banned")banned:Boolean = false,@Query("sort")sort:Boolean = true,@Query("\$top") top: Int = -1) : Deferred<Response<List<UserDTO>>>
//
//    @POST("")
//    fun initDraft(@Url url: String, @Query("fields") fields: String? = Fields.INIT_DRAFT,@Body createIssueDTO: DefaultCreateIssueBodyDTO?,@Query("top") top: Int = -1) : Deferred<Response<IssueDTO>>
//
//    @GET("")
//    fun getIssueByDraftId(@Url url: String, @Query("fields") fields: String? = Fields.INIT_DRAFT,@Query("top") top: Int = -1) : Deferred<Response<IssueDTO>>
//
//    @POST
//    fun updateDraft(@Url url: String,@Query("top") top: Int = -1,@Body updateDraftDTO: UpdateDraftDTO) : Deferred<Response<CustomFieldAdminDTO>>
}

private object Fields{
//
//    const val ISSUE_LIST = "comments,watchers(hasStar,id),reporter(id,login,name,avatarUrl),id,idReadable,summary,resolved,created,updated,description,type,fields(projectCustomField(field(name,value)),value(name,minutes,presentation,id,color(background,foreground)))"
//
//    const val PROJECT = "\$id,type,name,shortName,id"
//
//    const val CUSTOM_FIELDS = "type,name,id"
//
//    const val INIT_DRAFT = "id,idReadable,project(\$type,id,name,archived,shortName),fields(projectCustomField(field(id,name,value),canBeEmpty,id,emptyFieldText,bundle(id,isUpdateable)),value(name,minutes,fullName,ringId,avatarUrl,presentation,id,color(background,foreground)))"
//
    const val SERVER_CONFIG = "ring(url,serviceId),mobile(serviceSecret,serviceId),version,statisticsEnabled"
//
//    const val USER_CUSTOM_FIELD = "\$type,avatarUrl,fullName,name,id,ringId"
}