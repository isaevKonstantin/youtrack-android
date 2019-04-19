package com.konstantinisaev.youtrack.ui.base.viewmodels

import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import javax.inject.Inject

class GetProjectsViewModel @Inject constructor(private val apiProvider: ApiProvider,
                           private val basePreferencesAdapter: BasePreferencesAdapter,
                           coroutineContextHolder: CoroutineContextHolder) : BaseViewModel<String>(coroutineContextHolder) {

    override suspend fun execute(params: String?): ViewState {
        val projects = apiProvider.getProjects(basePreferencesAdapter.getUrl()).await()
        basePreferencesAdapter.setCurrentProjectId(projects.firstOrNull{ !it.archived }?.id.orEmpty())
        return ViewState.Success(this::class.java, projects)
    }


}