package com.konstantinisaev.youtrack.core.api.models

import com.konstantinisaev.youtrack.core.api.ProjectDTO

data class CachedPermissionDTO(val global: Boolean,val permission:Permission?,val projects: List<ProjectDTO>?)

data class Permission(val key: String?)

class PermissionHolder(){

    private val permissionsMap = mutableMapOf<String,CachedPermissionDTO>()

    fun init(permissions: List<CachedPermissionDTO>){
        permissions.forEach {
            val key = it.permission?.key.orEmpty()
            permissionsMap[key] = it
        }
    }

    fun hasPermission(permissionKey: String, projectRingId: String): Boolean {
        val cachedPermission = permissionsMap[permissionKey] ?: return false

        if(cachedPermission.global){
            return true
        }

        return cachedPermission.projects?.find { it.id == projectRingId } != null
    }

    fun canCreateIssue(projectId: String) = hasPermission(CREATE_ISSUE,projectId)

    fun isEmpty() = permissionsMap.isEmpty()

    companion object {

        const val CREATE_ISSUE = "JetBrains.YouTrack.CREATE_ISSUE"
    }
}