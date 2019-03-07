package com.konstantinisaev.youtrack.ui.base.viewmodels

sealed class ViewState(val owner: Class<in BaseViewModel<Any>>? = null){

    data class Init(val ownerClass: Class<BaseViewModel<Any>>? = null) : ViewState(ownerClass)
    data class Empty(val ownerClass: Class<BaseViewModel<Any>>? = null) : ViewState(ownerClass)
    data class Loading(val ownerClass: Class<BaseViewModel<Any>>? = null) : ViewState(ownerClass)
    data class Error(val ownerClass: Class<BaseViewModel<Any>>? = null) : ViewState(ownerClass)
    data class Success<T>(val ownerClass: Class<BaseViewModel<Any>>? = null,val data: T) : ViewState(ownerClass)
}