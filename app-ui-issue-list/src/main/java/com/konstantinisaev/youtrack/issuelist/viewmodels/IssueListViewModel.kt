package com.konstantinisaev.youtrack.issuelist.viewmodels

import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.core.api.DEFAULT_ISSUE_LIST_SIZE
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.models.Issue
import com.konstantinisaev.youtrack.ui.base.models.IssueUserField
import com.konstantinisaev.youtrack.ui.base.models.IssueWatcher
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
        return ViewState.Success(ownerClass = this::class.java,data = issues.map { Issue(it.id.orEmpty(),it.idReadable.orEmpty(),it.summary.orEmpty(),it.resolved.orEmpty(),it.created.orEmpty(),it.updated.orEmpty(),it.description.orEmpty(),
            					it.fields.orEmpty(), IssueUserField(it.reporter?.name.orEmpty(),it.reporter?.avatarUrl.orEmpty()),it.votes ?: 0, IssueWatcher(it.watchers?.id.orEmpty(),it.watchers?.hasStar == true),
            						it.comments.orEmpty(),null) })
    }
}

data class IssueListParam(val filter: String? = null,val top: Int = DEFAULT_ISSUE_LIST_SIZE, val skip: Int = 0)
