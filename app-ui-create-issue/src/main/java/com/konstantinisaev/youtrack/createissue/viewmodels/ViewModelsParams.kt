package com.konstantinisaev.youtrack.createissue.viewmodels

data class CreateIssueFieldParam(val parentType: String, val parentId: String)

data class UpdateDraftField(val fieldId: String,val valueId: String,val value: Map<String,Any>){

    companion object{

        const val PRESENTATION = "presentation"
        const val ID = "id"
    }
}