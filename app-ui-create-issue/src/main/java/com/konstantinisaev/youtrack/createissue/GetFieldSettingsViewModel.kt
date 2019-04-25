package com.konstantinisaev.youtrack.createissue

import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import javax.inject.Inject

class GetFieldSettingsViewModel @Inject constructor(private val apiProvider: ApiProvider,
                                                    private val basePreferencesAdapter: BasePreferencesAdapter,
                                                    coroutineContextHolder: CoroutineContextHolder) : BaseViewModel<GetFieldSettingsViewModel.Param>(coroutineContextHolder){

    override suspend fun execute(params: Param?): ViewState {
        checkNotNull(params)
        val fields = apiProvider.getCustomFieldSettings(basePreferencesAdapter.getUrl(),params.parentType,params.parentId).await()
        return ViewState.Success(this::class.java,fields)
    }

    data class Param(val parentType: String,val parentId: String)
}