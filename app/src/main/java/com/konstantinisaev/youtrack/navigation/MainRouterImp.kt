package com.konstantinisaev.youtrack.navigation

import androidx.fragment.app.Fragment
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.issuelist.AboutFragment
import com.konstantinisaev.youtrack.issuelist.AgileBoardsFragment
import com.konstantinisaev.youtrack.issuelist.IssueListFragment
import com.konstantinisaev.youtrack.issuelist.SettingsFragment
import com.konstantinisaev.youtrack.issuelist.di.IssueListDiProvider
import com.konstantinisaev.youtrack.ui.auth.CheckUrlFragment
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.utils.MainRouter
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import javax.inject.Inject

class MainRouterImp @Inject constructor(private val cicerone: Cicerone<Router>, basePreferencesAdapter: BasePreferencesAdapter,
                                        apiProvider: ApiProvider,
                                        coroutineContextHolder: CoroutineContextHolder) : MainRouter{
    override fun showServerUrl() {
        cicerone.router.newRootScreen(object : SupportAppScreen(){
            override fun getFragment(): Fragment {
                return CheckUrlFragment()
            }
        })
    }

    init {
        IssueListDiProvider.init(this,apiProvider,basePreferencesAdapter,coroutineContextHolder,cicerone)
    }

    override fun showIssueList() {
        showFragment(IssueListFragment())
    }

    override fun showAgileBoards() {
        showFragment(AgileBoardsFragment())
    }

    override fun showSettings() {
        showFragment(SettingsFragment())
    }

    override fun showAbout() {
        showFragment(AboutFragment())
    }

    private fun showFragment(fragment: Fragment){
        cicerone.router.navigateTo(object : SupportAppScreen(){
            override fun getFragment(): Fragment {
                return fragment
            }
        })

    }

}