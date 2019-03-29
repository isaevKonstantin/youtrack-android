package com.konstantinisaev.youtrack.ui.base.utils

object Routers {

    lateinit var authRouter: AuthRouter
}

interface AuthRouter {

    fun showSplash()

    fun showServerUrl()

    fun showAuth()

    fun showMain()
}