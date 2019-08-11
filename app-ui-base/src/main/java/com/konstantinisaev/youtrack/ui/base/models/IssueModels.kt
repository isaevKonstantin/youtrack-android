@file:Suppress("unused")

package com.konstantinisaev.youtrack.ui.base.models

import com.konstantinisaev.youtrack.core.api.*
import com.konstantinisaev.youtrack.ui.base.utils.DateUtils
import com.konstantinisaev.youtrack.ui.base.utils.ObjectMapper
import java.util.*

@Suppress("UNCHECKED_CAST")
data class Issue(val id: String, val idReadable: String, val summary: String, val resolved: String, val created: String, val updated: String, val description: String,
				 private val fields: List<FieldContainerDTO>, val reporter: IssueUserField, val votes: Int, val watchers: IssueWatcher, val comments: List<CommentDTO>, val project: Project?){

	val mappedCreatedDate: Date? = DateUtils.toDate(created.toLongOrNull())
	val mappedUpdatedDate: Date? = DateUtils.toDate(updated.toLongOrNull())
	val mappedResolvedDate: Date? = DateUtils.toDate(resolved.toLongOrNull())

	var priority: FieldContainer<EnumValue> = FieldContainer(ProjectCustomFieldDto(), EnumValue("",FieldColor()))
		private set
	var type: FieldContainer<EnumValue> = FieldContainer(ProjectCustomFieldDto(), EnumValue("",FieldColor()))
		private set
	var state: FieldContainer<EnumValue> = FieldContainer(ProjectCustomFieldDto(), EnumValue("",FieldColor()))
		private set
	var assignee: FieldContainer<UserValue> = FieldContainer(ProjectCustomFieldDto(), UserValue())
		private set
	var estimation: FieldContainer<TimeValue> = FieldContainer(ProjectCustomFieldDto(),TimeValue())
		private set
	var spentTime: FieldContainer<TimeValue> = FieldContainer(ProjectCustomFieldDto(), TimeValue())
		private set
	var sprint: FieldContainer<EnumValue> = FieldContainer(ProjectCustomFieldDto(), EnumValue("",FieldColor()))
		private set

	init {
		fields.forEach {
			val fieldName = it.projectCustomField?.field?.name.orEmpty()
			val field = mapFieldContainer(it)
			field?.let { notNullField ->
				when(fieldName.toLowerCase()) {
					PRIORITY_FIELD -> priority = notNullField as FieldContainer<EnumValue>
					TYPE_FIELD -> type = notNullField as FieldContainer<EnumValue>
					STATE_FIELD -> state = notNullField as FieldContainer<EnumValue>
					SPRINT_FIELD -> sprint = notNullField as FieldContainer<EnumValue>
					ASSIGNEE_FIELD -> assignee = notNullField as FieldContainer<UserValue>
					SPENT_TIME_FIELD -> spentTime = notNullField as FieldContainer<TimeValue>
					ESTIMATION_FIELD -> estimation = notNullField as FieldContainer<TimeValue>
				}
			}
		}
	}

	fun isDone() = state.value.name.toLowerCase() == "done"

}

data class IssueFilterSuggest(val styleClass: String? = null, val prefix: String? = null, val option: String? = null,
							  val suffix: String? = null, val description: String? = null, val htmlDescription: String? = null,
							  val caret: Int? = null, val completion: FilterMatchDTO? = null, val matchDTO: FilterMatchDTO? = null, val uuid:String = UUID.randomUUID().toString(),
							  val parentUuid: String = "", val checked: Boolean = false)


data class FieldContainer<T>(val projectCustomField: ProjectCustomFieldDto,val value: T){

	fun paramForCreateIssueReq() = when {
		isEnum(projectCustomField.type.orEmpty()) -> "enum"
		isState(projectCustomField.type.orEmpty()) -> "state"
		isVersion(projectCustomField.type.orEmpty()) -> "version"
		else -> ""
	}

	companion object {
		fun isEnum(fieldName: String) = fieldName.contains("EnumProjectCustomField")
		fun isState(fieldName: String) = fieldName.contains("StateProjectCustomField")
		fun isUser(fieldName: String) = fieldName.contains("UserProjectCustomField")
		fun isTime(fieldName: String) = fieldName.contains("PeriodProjectCustomField")
		fun isVersion(fieldName: String) = fieldName.contains("VersionProjectCustomField")
	}
}
data class EnumValue(val name: String, val fieldColor: FieldColor)
data class TimeValue(val minutes: Int = 0, val presentation: String = "")
data class UserValue(val name: String = "",val avatarUrl: String = "")

class IssueUserField(val name: String,
                     val avatarUrl: String,
                     fieldId: String = "",
                     id: String = "",
                     canBeEmpty: Boolean = true,
                     emptyFieldText: String = "",
                     bundleId: String = "",
                     fieldName: String = "",
                     type: String = "")

data class IssueWatcher(val id: String?,val hasStar: Boolean)

data class IssueComment(val id: String?)

data class FieldColor(val background: String = "", val foreground: String = ""){

	val default = background.isEmpty() && foreground.isEmpty()

	val isWhiteBackground = background.toLowerCase() == "#ffffff"
}

data class Project(val id: String,val name: String,val shortName: String,val archived: Boolean,val type: String)


private const val PRIORITY_FIELD = "priority"
private const val TYPE_FIELD = "type"
private const val STATE_FIELD = "state"
private const val ASSIGNEE_FIELD = "assignee"
private const val ESTIMATION_FIELD = "estimation"
private const val SPENT_TIME_FIELD = "spent time"
private const val SPRINT_FIELD = "sprints"

fun mapIssue(issueDTO: IssueDTO) = Issue(issueDTO.id.orEmpty(),issueDTO.idReadable.orEmpty(),issueDTO.summary.orEmpty(),issueDTO.resolved.orEmpty(),issueDTO.created.orEmpty(),issueDTO.updated.orEmpty(),
	issueDTO.description.orEmpty(),issueDTO.fields.orEmpty(),
	IssueUserField(issueDTO.reporter?.name.orEmpty(),issueDTO.reporter?.avatarUrl.orEmpty()),
	issueDTO.votes ?: 0,
	IssueWatcher(issueDTO.watchers?.id.orEmpty(),issueDTO.watchers?.hasStar == true),
	Collections.emptyList(),
	Project(issueDTO.project?.id.orEmpty(),issueDTO.project?.name.orEmpty(),issueDTO.project?.shortName.orEmpty(),issueDTO.project?.archived == true,issueDTO.project?.type.orEmpty())
)

fun mapFieldContainer(fieldContainerDTO: FieldContainerDTO) : FieldContainer<*>?{
	return when{
		FieldContainer.isEnum(fieldContainerDTO.projectCustomField?.type.orEmpty()) || FieldContainer.isState(fieldContainerDTO.projectCustomField?.type.orEmpty()) || FieldContainer.isVersion(fieldContainerDTO.projectCustomField?.type.orEmpty())-> {
			val parsedValue = ObjectMapper.mapCustomFieldFromJson(fieldContainerDTO.value)
			FieldContainer(fieldContainerDTO.projectCustomField ?: ProjectCustomFieldDto(),EnumValue(parsedValue.name,FieldColor(parsedValue.colors.first,parsedValue.colors.second)))
		}
		FieldContainer.isUser(fieldContainerDTO.projectCustomField?.type.orEmpty()) -> {
			val parsedValue = ObjectMapper.mapUserFieldFromJson(fieldContainerDTO.value)
			FieldContainer(fieldContainerDTO.projectCustomField ?: ProjectCustomFieldDto(),UserValue(parsedValue.name,parsedValue.avatarUrl))
		}
		FieldContainer.isTime(fieldContainerDTO.projectCustomField?.type.orEmpty()) -> {
			val parsedValue = ObjectMapper.mapPeriodicFieldFromJson(fieldContainerDTO.value)
			FieldContainer(fieldContainerDTO.projectCustomField ?: ProjectCustomFieldDto(),TimeValue(parsedValue.minutes,parsedValue.presentation))
		}else -> null
	}
}
