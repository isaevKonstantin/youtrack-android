package com.konstantinisaev.youtrack.issuelist.viewmodels

import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import javax.inject.Inject

class IssueSavedFilterViewModel @Inject constructor(private val basePreferencesAdapter: BasePreferencesAdapter,
                                                    coroutineHolder: CoroutineContextHolder
) : BaseViewModel<String>(coroutineHolder) {

    override suspend fun execute(params: String?): ViewState {
        return ViewState.Success(this::class.java,IssueFilterResultDTO(basePreferencesAdapter.getSavedQuery(),basePreferencesAdapter.getSortQuery()))
    }

    data class IssueFilterResultDTO(val filterReq: String,val sortReq: String)
}

