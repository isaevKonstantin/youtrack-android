package com.konstantinisaev.youtrack.createissue.viewmodels

data class CreateIssueFieldParam(val parentType: String, val parentId: String)

data class UpdateDraftField(val fieldId: String,val valueId: String)