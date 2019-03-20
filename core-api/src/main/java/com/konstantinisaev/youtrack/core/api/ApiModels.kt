package com.konstantinisaev.youtrack.core.api

data class ServerVersionDTO @JvmOverloads constructor(var id: Int = 0, var name: String? = null, var version: String? = null, var build: Int = 0)

data class ServerConfigDTO(val version: String, val mobile: MobileConfigDTO, val ring: RingConfigDTO, val statisticsEnabled: Boolean, val type: String)

data class MobileConfigDTO(val serviceSecret: String, val serviceId: String, val type:String)

data class RingConfigDTO(val serviceId: String, val url: String, val type: String)
//
//data class RespAuthToken(@SerializedName("access_token") val accessToken: String, @SerializedName("token_type") val tokenType: String,
//                         @SerializedName("expires_in")val expiresIn: Int, @SerializedName("refresh_token") val refreshToken: String, val scope: String)
//
//data class CurrentUserDTO(val id: String?, val fullName: String?, val login: String?, val email: String?, val guest: Boolean, val online: Boolean, val avatar: String?, val avatarUrl: String?)
//
//data class ProjectDTO(val id: String?, val name: String?, val shortName: String?,val type: String?,val archived: Boolean)
//
//data class IssueDTO(val id: String?, val idReadable: String?, val summary: String?, val resolved: String?, val created: String?, val updated: String?, val description: String?,
//                    val fields: List<FieldContainerDTO>?, val reporter: UserDTO?, val votes: Int?, val watchers: WatcherDTO?, val comments: List<CommentDTO>?, val project: ProjectDTO)
//
//data class FieldContainerDTO(val projectCustomField: ProjectCustomFieldDto? = null, val value: JsonElement? = null)
//
//data class ProjectCustomFieldDto(val field: CustomFieldDTO? = null, val bundle: BundleDTO? = null, val emptyFieldText: String? = null, val canBeEmpty: Boolean = false,
//                                 @SerializedName("\$type")val type: String? = null,val id: String? = null)
//
//data class CustomFieldDTO(val name: String?, val id: String?)
//
//data class BundleDTO(val id: String?)
//
//data class UserDTO(val id: String?,val ringId: String?,val login: String?,val name: String?,val avatarUrl: String?,val fullName: String?)
//
//data class WatcherDTO(val id: String?,val hasStar: Boolean)
//
//data class CommentDTO(val id: String?)
//
//data class CreateIssueDTO(val project: IssueProjectDTO,val summary: String)
//
//data class IssueProjectDTO(val id: String)
//
//data class CustomFieldAdminDTO(val id: String?,val name: String?,@SerializedName("\$type")val type: String?,val description: String?)
//
//data class UpdateTimeFieldDTO(val presentation: String?)
//
//data class DefaultCreateIssueBodyDTO(private val id: String? = "",private val summary: String? = "",private val description: String? = "",private val project: CreateIssueProjectDTO)
//
//data class UpdateDraftDTO(private val id: String? = "", val value: Any)
//
//data class CreateIssueProjectDTO(private val name:String? = "Not selected",private val id: String?)
//
//data class RespIssueFilterIntellisense(val suggest: SuggestItems,val recent: List<FilterSuggest>,val highlight: List<FilterSuggest>)
//
//data class FilterSuggest(val styleClass: String?,@SerializedName("pre") val prefix: String?, @SerializedName("o") val option: String?,
//                         @SerializedName("suf") val suffix: String?,@SerializedName("d") val description: String?,@SerializedName("hd") val htmlDescription: String?,
//                         val caret: Int?, val completion: FilterCompletion, val match: FilterMatch)
//
//data class FilterCompletion(val start: Int,val end: String)
//
//data class FilterMatch(val start: Int,val end: String)
//
//data class RespIssueCountDTO(val value: Int)
//
//data class SuggestItems(val items: List<FilterSuggest>)