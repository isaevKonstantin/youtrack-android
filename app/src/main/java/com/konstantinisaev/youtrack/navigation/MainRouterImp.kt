package com.konstantinisaev.youtrack.navigation

import androidx.fragment.app.Fragment
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.issuelist.IssueListFragment
import com.konstantinisaev.youtrack.issuelist.di.IssueListDiProvider
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.utils.MainRouter
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import javax.inject.Inject

class MainRouterImp @Inject constructor(private val cicerone: Cicerone<Router>, basePreferencesAdapter: BasePreferencesAdapter,
                                        apiProvider: ApiProvider,
                                        coroutineContextHolder: CoroutineContextHolder) : MainRouter{

    init {
        IssueListDiProvider.init(this,apiProvider,basePreferencesAdapter,coroutineContextHolder,cicerone)
    }

    override fun showIssueList() {
        cicerone.router.replaceScreen(object : SupportAppScreen(){
            override fun getFragment(): Fragment {
                return IssueListFragment()
            }
        })
    }

    override fun showAgileBoards() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSettings() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAbout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}