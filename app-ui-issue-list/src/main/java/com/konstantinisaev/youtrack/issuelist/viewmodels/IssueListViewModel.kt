package com.konstantinisaev.youtrack.issuelist.viewmodels

import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.core.api.DEFAULT_ISSUE_LIST_SIZE
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.models.mapIssue
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import javax.inject.Inject

class IssueListViewModel @Inject constructor(private val apiProvider: ApiProvider,
                                             private val basePreferencesAdapter: BasePreferencesAdapter,
                                             coroutineHolder: CoroutineContextHolder
) : BaseViewModel<IssueListParam>(coroutineHolder) {

    override suspend fun execute(params: IssueListParam?): ViewState {
        val notNullParams = checkNotNull(params)
        val issues = apiProvider.getAllIssues(basePreferencesAdapter.getUrl(),notNullParams.filter,notNullParams.top,notNullParams.skip).await()
        return ViewState.Success(ownerClass = this::class.java,data = issues.map { mapIssue(it) })
    }
}

data class IssueListParam(val filter: String? = null,val top: Int = DEFAULT_ISSUE_LIST_SIZE, val skip: Int = 0)
