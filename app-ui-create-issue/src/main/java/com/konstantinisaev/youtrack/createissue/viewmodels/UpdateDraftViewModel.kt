package com.konstantinisaev.youtrack.createissue.viewmodels

import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.core.api.UpdateDraftDTO
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.models.Issue
import com.konstantinisaev.youtrack.ui.base.models.mapIssue
import com.konstantinisaev.youtrack.ui.base.models.mapProject
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import javax.inject.Inject

class UpdateDraftViewModel @Inject constructor(private val apiProvider: ApiProvider,
                                               private val basePreferencesAdapter: BasePreferencesAdapter,
                                               coroutineContextHolder: CoroutineContextHolder
) : BaseViewModel<Issue>(coroutineContextHolder) {

    override suspend fun execute(params: Issue?): ViewState {
        checkNotNull(params)
        val updatedIssue = apiProvider.updateDraft(basePreferencesAdapter.getUrl(), basePreferencesAdapter.getLastDraftId(),
            UpdateDraftDTO(params.id,params.summary,params.description, mapProject(params.project))
        ).await()
        return ViewState.Success(this::class.java, mapIssue(updatedIssue))
    }
}