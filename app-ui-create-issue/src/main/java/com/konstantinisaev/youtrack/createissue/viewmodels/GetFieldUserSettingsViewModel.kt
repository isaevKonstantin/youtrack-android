package com.konstantinisaev.youtrack.createissue.viewmodels

import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import javax.inject.Inject

class GetFieldUserSettingsViewModel @Inject constructor(private val apiProvider: ApiProvider,
                                                        private val basePreferencesAdapter: BasePreferencesAdapter,
                                                        coroutineContextHolder: CoroutineContextHolder) : BaseViewModel<CreateIssueFieldParam>(coroutineContextHolder){

    override suspend fun execute(params: CreateIssueFieldParam?): ViewState {
        checkNotNull(params)
        val result = apiProvider.getCustomFieldUserSettings(basePreferencesAdapter.getUrl(), params.parentType, params.parentId).await()
        return ViewState.Success(this::class.java,result)
    }
}