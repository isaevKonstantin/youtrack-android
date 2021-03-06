package com.konstantinisaev.youtrack.ui.base.viewmodels

sealed class ViewState(val owner: Class<out BaseViewModel<*>>? = null){

    data class Init(val ownerClass: Class<BaseViewModel<Any>>? = null) : ViewState(ownerClass)
    data class Empty(val ownerClass: Class<out BaseViewModel<*>>? = null) : ViewState(ownerClass)
    data class Error(val ownerClass: Class<out BaseViewModel<*>>? = null,val reasonException: Throwable? = null) : ViewState(ownerClass)
    data class ValidationError(val ownerClass: Class<out BaseViewModel<*>>? = null,val msgId: Int) : ViewState(ownerClass)
    data class Success<T>(val ownerClass: Class<out BaseViewModel<*>>? = null,val data: T) : ViewState(ownerClass)
}