package com.konstantinisaev.youtrack.ui.base.viewmodels

import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.core.api.ProjectDTO
import com.konstantinisaev.youtrack.core.api.UrlFormatter
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
        val baseUrl = basePreferencesAdapter.getUrl()
        if(permissionHolder.isEmpty()){
            val config = basePreferencesAdapter.getServerConfig() ?: return ViewState.Error(this.javaClass)
            val permissions = apiProvider.getPermissions(UrlFormatter.formatToPermissionUrl(basePreferencesAdapter.getUrl(),config.ring.url),config.ring.serviceId).await()
            permissionHolder.init(permissions)
        }
        val projects = apiProvider.getProjects(baseUrl).await()
        val availableProjects = projects.filter { !it.archived && permissionHolder.canCreateIssue(it.ringId.orEmpty()) }
        basePreferencesAdapter.setCurrentProjectId(availableProjects.firstOrNull()?.id.orEmpty())
        this.projects.clear()
        this.projects.addAll(availableProjects)
        return ViewState.Success(this::class.java, availableProjects)
    }


}