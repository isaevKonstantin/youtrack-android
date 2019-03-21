package com.konstantinisaev.youtrack.core.api

enum class ApiEndpoints(val url: String){
	YOUTRACK("youtrack"),
	SERVER_CONFIG("api/config?fields=ring(url,serviceId),mobile(serviceSecret,serviceId),version,statisticsEnabled"),
	VERSION("rest/workflow/version"),
	CONFIG("api/config"),
	LOGIN("api/rest/oauth2/token"),

	GET_CURRENT_USER("rest/user/current"),
	GET_PROJECTS("api/admin/projects"),
	GET_CUSTOM_FIELD_SETTING("api/admin/customFieldSettings/bundles/%s/%s/values"),
	GET_USER_CUSTOM_FIELD_SETTING("api/admin/customFieldSettings/bundles/%s/%s/aggregatedUsers"),
	GET_ALL_ISSUES("api/issues"),
	GET_ISSUE_FILTER_INTELLISENCE("rest/search/underlineAndSuggest"),
	GET_ALL_ISSUES_COUNT("rest/issue/count"),
	DRAFT("api/admin/users/me/drafts/%s"),
	UPDATE_FIELD("${DRAFT.url}/fields/%s")
}
