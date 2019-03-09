package com.konstantinisaev.youtrack.ui.base.utils

object Routers{

    lateinit var splashRouter: SplashRouter
}

interface SplashRouter {

    fun showSplash()

    fun showServerUrl()
}