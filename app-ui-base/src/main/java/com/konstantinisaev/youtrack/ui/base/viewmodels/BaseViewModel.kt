package com.konstantinisaev.youtrack.ui.base.viewmodels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.ui.base.utils.Validator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel<P>(private val coroutineContextHolder: CoroutineContextHolder? = null,private val validator: Validator<P>? = null) : ViewModel() {

    private val liveData = SingleLiveEvent<ViewState>()

    private lateinit var job: Job

    var lastViewState: ViewState = ViewState.Init()
        protected set

    init {
        reinitializeJob()
    }

    fun doAsyncRequest(params: P? = null){
        lastViewState = ViewState.Empty()
        validator?.apply {
            validate(params).takeIf { !it }?.let {
                lastViewState = ViewState.ValidationError(this@BaseViewModel::class.java,msgId = validator.errorId)
                liveData.postValue(lastViewState)
                return
            }
        }
        if(job.isCompleted){
            reinitializeJob()
        }
        coroutineContextHolder ?: let {
            lastViewState = ViewState.Error(this@BaseViewModel::class.java,reasonException = RuntimeException("Coroutine context holder is null"))
            liveData.postValue(lastViewState)
            return
        }
        GlobalScope.launch(coroutineContextHolder.io() + job) {
            try {
                val resp = execute(params)
                lastViewState = resp
                liveData.postValue(resp)
            }catch (ex: Exception){
                ex.printStackTrace()
                lastViewState = ViewState.Error(this@BaseViewModel::class.java,reasonException = ex)
                liveData.postValue(lastViewState)
            }
        }
    }

    open fun changeViewState(viewState: ViewState){
        liveData.value = viewState
    }

    abstract suspend fun execute(params: P? = null) : ViewState

    open fun observe(owner: LifecycleOwner,observer: Observer<ViewState>){
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