package com.konstantinisaev.youtrack.ui.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel<P,R> : ViewModel() {

    private val liveData = SingleLiveEvent<R>()
    private lateinit var job: Job

    init {
        reinitializeJob()
    }

    fun doAsyncRequest(params: P){
        if(job.isCompleted){
            reinitializeJob()
        }
        GlobalScope.launch(Dispatchers.IO + job) {
            val resp = execute(params)
            liveData.postValue(resp)
        }
    }

    abstract fun execute(params: P) : R

    fun observe(owner: LifecycleOwner,observer: Observer<R>){
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