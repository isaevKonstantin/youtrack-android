package com.konstantinisaev.youtrack.createissue.viewmodels

import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.createissue.R
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.models.mapIssue
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import javax.inject.Inject

class DraftViewModel @Inject constructor(val apiProvider: ApiProvider,
                                         val basePreferencesAdapter: BasePreferencesAdapter,
                                         coroutineContextHolder: CoroutineContextHolder): BaseViewModel<String>(coroutineContextHolder) {

    override suspend fun execute(params: String?): ViewState {

        val draftId = basePreferencesAdapter.getLastDraftId()
        val currentProjectId = basePreferencesAdapter.getCurrentProjectId()
        val url = basePreferencesAdapter.getUrl()
        if(draftId.isEmpty() && currentProjectId.isEmpty()){
            return ViewState.ValidationError(this::class.java,
                R.string.create_issue_fragm_cannot_create_issue_error
            )
        }
        val issueDTO = if(draftId.isNotEmpty()){
            try {
                apiProvider.getIssueByDraftId(url,draftId).await()
            }catch (ex: Exception){
                apiProvider.initDraft(url,currentProjectId).await()
            }
        }else{
            apiProvider.initDraft(url,currentProjectId).await()
        }
        basePreferencesAdapter.setLastDraftId(issueDTO.id.orEmpty())
        return ViewState.Success(this::class.java, mapIssue(issueDTO))
    }
}