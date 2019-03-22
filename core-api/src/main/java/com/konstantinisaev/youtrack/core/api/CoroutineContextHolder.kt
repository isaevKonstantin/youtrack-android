package com.konstantinisaev.youtrack.core.api

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class CoroutineContextHolder {

	open fun main(): CoroutineDispatcher = Dispatchers.Main

	open fun io(): CoroutineDispatcher = Dispatchers.IO
}