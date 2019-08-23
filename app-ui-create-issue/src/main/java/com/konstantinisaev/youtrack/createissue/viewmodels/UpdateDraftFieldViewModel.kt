package com.konstantinisaev.youtrack.createissue.viewmodels

import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.models.mapFieldContainer
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import javax.inject.Inject

class UpdateDraftFieldViewModel @Inject constructor(private val apiProvider: ApiProvider,
                                                    private val basePreferencesAdapter: BasePreferencesAdapter,
                                                    coroutineContextHolder: CoroutineContextHolder
) : BaseViewModel<UpdateDraftField>(coroutineContextHolder) {

    override suspend fun execute(params: UpdateDraftField?): ViewState {
        checkNotNull(params)
        val updatedField = apiProvider.updateDraftField(
            basePreferencesAdapter.getUrl(),
            basePreferencesAdapter.getLastDraftId(),
            params.fieldId,
            params.value
        ).await()
        return ViewState.Success(this::class.java, mapFieldContainer(updatedField))
    }
}