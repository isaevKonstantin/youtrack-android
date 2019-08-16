package com.konstantinisaev.youtrack.core.api

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class ServerVersionDTO @JvmOverloads constructor(var id: Int = 0, var name: String? = null, var version: String? = null, var build: Int = 0)

data class ServerConfigDTO(val version: String, val mobile: MobileConfigDTO, val ring: RingConfigDTO, val statisticsEnabled: Boolean, val type: String)

data class MobileConfigDTO(val serviceSecret: String, val serviceId: String, val type:String)

data class RingConfigDTO(val serviceId: String, val url: String, val type: String)

data class AuthTokenDTO(@SerializedName("access_token") val accessToken: String, @SerializedName("token_type") val tokenType: String,
                        @SerializedName("expires_in")val expiresIn: Int, @SerializedName("refresh_token") val refreshToken: String, val scope: String)

data class CurrentUserDTO(val id: String?, val fullName: String?, val login: String?, val email: String?, val guest: Boolean, val online: Boolean, val avatar: String?, val avatarUrl: String?) {

    lateinit var formattedImageUrl: String

    lateinit var initials: String
}

data class ProjectDTO(val id: String?, val name: String?, val shortName: String?,val type: String?,val archived: Boolean,val ringId: String?)

data class IssueDTO(val id: String? = null, val idReadable: String? = null, val summary: String? = null, val resolved: String? = null, val created: String? = null, val updated: String? = null, val description: String? = null,
                    val fields: List<FieldContainerDTO>? = null, val reporter: UserDTO? = null, val votes: Int? = null, val watchers: WatcherDTO? = null, val comments: List<CommentDTO>? = null, val project: ProjectDTO? = null)

data class FieldContainerDTO(val projectCustomField: ProjectCustomFieldDto? = null, val value: JsonElement? = null)

data class ProjectCustomFieldDto(val field: CustomFieldDTO? = null, val bundle: BundleDTO? = null, val emptyFieldText: String? = null, val canBeEmpty: Boolean = false,
                                 @SerializedName("\$type")val type: String? = null,val id: String? = null)

data class CustomFieldDTO(val name: String?, val id: String?,val fieldType: FieldTypeDTO? = null)

data class BundleDTO(val id: String?)

data class UserDTO(val id: String?,val ringId: String?,val login: String?,val name: String?,val avatarUrl: String?,val fullName: String?){

    lateinit var initials: String
}

data class WatcherDTO(val id: String?,val hasStar: Boolean)

data class CommentDTO(val id: String?)

data class IssueCountDTO(val value: Int)

data class CreateIssueDTO(val project: IssueProjectDTO,val summary: String)

data class IssueProjectDTO(val id: String)

data class CustomFieldAdminDTO(val id: String?,val name: String?,@SerializedName("\$type")val type: String?,val description: String?)

data class UpdateTimeFieldDTO(val presentation: String?)

data class FieldTypeDTO(val isMultiValue: Boolean,val valueType: String?)

data class DefaultCreateIssueBodyDTO(private val id: String? = "",private val summary: String? = "",private val description: String? = "",private val project: CreateIssueProjectDTO)

data class UpdateDraftFieldDTO(private val id: String? = "", val value: Any)

data class UpdateProjectFieldDTO(private val id: String? = "", val value: Any)

data class UpdateDraftDTO(private val id: String? = "",private val summary: String? = "",private val description: String? = "",private val project: ProjectDTO? = null)

data class CreateIssueProjectDTO(private val name:String? = "Not selected",private val id: String?)

data class FilterAutoCompleteDTO(@SerializedName("suggest") val suggestDTO: SuggestItemsDTO, val recent: List<FilterSuggestDTO>, val highlight: List<FilterSuggestDTO>)

data class FilterSuggestDTO(val styleClass: String?, @SerializedName("pre") val prefix: String?, @SerializedName("o") val option: String?,
                            @SerializedName("suf") val suffix: String?, @SerializedName("d") val description: String?, @SerializedName("hd") val htmlDescription: String?,
                            val caret: Int?, val completion: FilterMatchDTO, val match: FilterMatchDTO)

data class FilterMatchDTO(val start: Int, val end: String)

data class SuggestItemsDTO(val items: List<FilterSuggestDTO>)