package com.konstantinisaev.youtrack.ui.base.viewmodels

import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.core.api.ProjectDTO
import com.konstantinisaev.youtrack.core.api.models.PermissionHolder
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import javax.inject.Inject

class GetProjectsViewModel @Inject constructor(private val apiProvider: ApiProvider,
                                               private val basePreferencesAdapter: BasePreferencesAdapter,
                                               private val permissionHolder: PermissionHolder,
                                               coroutineContextHolder: CoroutineContextHolder) : BaseViewModel<String>(coroutineContextHolder) {

    val projects = mutableListOf<ProjectDTO>()

    override suspend fun execute(params: String?): ViewState {
        if(this.projects.isNotEmpty()){
            return ViewState.Success(this::class.java, this.projects)
        }
        val projects = apiProvider.getProjects(basePreferencesAdapter.getUrl()).await()
        val currentProject = projects.firstOrNull { !it.archived && permissionHolder.canCreateIssue(it.ringId.orEmpty()) }
        basePreferencesAdapter.setCurrentProjectId(currentProject?.id.orEmpty())
        this.projects.clear()
        this.projects.addAll(projects)
        return ViewState.Success(this::class.java, projects)
    }


}