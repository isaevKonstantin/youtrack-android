package com.konstantinisaev.youtrack.createissue.viewmodels

import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.utils.InitialsConvertor
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import javax.inject.Inject

class GetFieldUserSettingsViewModel @Inject constructor(private val apiProvider: ApiProvider,
                                                        private val basePreferencesAdapter: BasePreferencesAdapter,
                                                        coroutineContextHolder: CoroutineContextHolder) : BaseViewModel<CreateIssueFieldParam>(coroutineContextHolder){

    override suspend fun execute(params: CreateIssueFieldParam?): ViewState {
        checkNotNull(params)
        val users = apiProvider.getCustomFieldUserSettings(basePreferencesAdapter.getUrl(), params.parentType, params.parentId).await()
        users.forEach { it.initials = InitialsConvertor.convertToInitials(it.fullName.orEmpty()) }
        return ViewState.Success(this::class.java,users)
    }
}