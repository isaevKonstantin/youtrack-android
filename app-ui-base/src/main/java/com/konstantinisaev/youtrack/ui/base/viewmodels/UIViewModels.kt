package com.konstantinisaev.youtrack.ui.base.viewmodels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import javax.inject.Inject

open class BaseUiViewModel : BaseViewModel<String>(){

    private val tmp = MutableLiveData<ViewState>()

    override suspend fun execute(params: String?): ViewState {
        return ViewState.Empty(this::class.java)
    }

    override fun changeViewState(viewState: ViewState) {
        tmp.postValue(viewState)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<ViewState>) {
        tmp.observe(owner,observer)
    }
}

class FilterUpdatedViewModel @Inject constructor() : BaseUiViewModel()