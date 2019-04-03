package com.konstantinisaev.youtrack.ui.base.utils

interface AuthRouter {

    fun showSplash()

    fun showServerUrl()

    fun showAuth()

    fun showMain()
}

interface MainRouter {

    fun showIssueList()

    fun showAgileBoards()

    fun showSettings()

    fun showAbout()

    fun showServerUrl()
}