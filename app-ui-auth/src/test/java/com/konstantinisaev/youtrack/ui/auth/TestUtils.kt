package com.konstantinisaev.youtrack.ui.auth

import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
val testCoroutineContextHolder = object : CoroutineContextHolder() {
    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun main(): CoroutineDispatcher = Dispatchers.Unconfined
}
