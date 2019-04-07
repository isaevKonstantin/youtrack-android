package com.konstantinisaev.youtrack.navigation

import androidx.fragment.app.Fragment
import com.konstantinisaev.youtrack.issuelist.IssueFilterFragment
import com.konstantinisaev.youtrack.ui.base.utils.IssueListRouter
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import javax.inject.Inject

class IssueListRouterImp @Inject constructor(private val cicerone: Cicerone<Router>
) : IssueListRouter {

    override fun setNavigator(navigator: Navigator) {
        cicerone.navigatorHolder.setNavigator(navigator)
    }

    override fun showFilter() {
        cicerone.router.navigateTo(object : SupportAppScreen(){
            override fun getFragment(): Fragment {
                return IssueFilterFragment()
            }
        })
    }

    override fun showSort() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}