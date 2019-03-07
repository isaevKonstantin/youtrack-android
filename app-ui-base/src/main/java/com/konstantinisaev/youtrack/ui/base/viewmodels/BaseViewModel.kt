package com.konstantinisaev.youtrack.ui.base.viewmodels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel<P> : ViewModel() {

    private val liveData = SingleLiveEvent<ViewState>()
    private lateinit var job: Job
    var lastViewState: ViewState = ViewState.Init()
        protected set

    init {
        reinitializeJob()
    }

    fun doAsyncRequest(params: P? = null){
        if(job.isCompleted){
            reinitializeJob()
        }
        GlobalScope.launch(Dispatchers.IO + job) {
            val resp = execute(params)
            lastViewState = resp
            liveData.postValue(resp)
        }
    }

    abstract fun execute(params: P? = null) : ViewState

    fun observe(owner: LifecycleOwner,observer: Observer<ViewState>){
        liveData.observe(owner,observer)
    }

    private fun reinitializeJob(){
        job = Job()
    }

    override fun onCleared() {
        super.onCleared()
        if(job.isActive){
            job.cancel()
        }
    }

}