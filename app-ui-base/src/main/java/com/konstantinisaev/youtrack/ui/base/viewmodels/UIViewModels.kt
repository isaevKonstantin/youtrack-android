package com.konstantinisaev.youtrack.ui.base.viewmodels

import javax.inject.Inject

open class BaseUiViewModel : BaseViewModel<String>(){

    override suspend fun execute(params: String?): ViewState {
        return ViewState.Empty(this::class.java)
    }

}

class FilterUpdatedViewModel @Inject constructor() : BaseUiViewModel()

class UpdateIssueListViewModel @Inject constructor() : BaseUiViewModel()