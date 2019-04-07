package com.konstantinisaev.youtrack.ui.base.utils

import ru.terrakok.cicerone.Navigator

interface BaseRouter {

    fun setNavigator(navigator: Navigator)
}

interface AuthRouter : BaseRouter{

    fun showSplash()

    fun showServerUrl()

    fun showAuth()

    fun showMain()
}

interface MainRouter : BaseRouter{

    fun showIssueList()

    fun showAgileBoards()

    fun showSettings()

    fun showAbout()

    fun showServerUrl()

}

interface IssueListRouter : BaseRouter{

    fun showFilter()

    fun showSort()
}