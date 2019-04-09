package com.konstantinisaev.youtrack.ui.base.viewmodels

import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import javax.inject.Inject

class IssueCountViewModel @Inject constructor(private val apiProvider: ApiProvider,
                                              private val basePreferencesAdapter: BasePreferencesAdapter,
                                              coroutineHolder: CoroutineContextHolder
) : BaseViewModel<String>(coroutineHolder) {

    override suspend fun execute(params: String?): ViewState {
        val notNullParams = checkNotNull(params)
        var issueCount = apiProvider.getAllIssuesCount(basePreferencesAdapter.getUrl(),notNullParams).await()
        var counter = 0
        while (issueCount.value < 0 && counter < 2){
            issueCount = apiProvider.getAllIssuesCount(basePreferencesAdapter.getUrl(),notNullParams).await()
            counter += 1
        }
        return ViewState.Success(this::class.java,issueCount)
    }
}