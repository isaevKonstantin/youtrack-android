package com.konstantinisaev.youtrack.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.konstantinisaev.youtrack.issuefilter.IssueFilterFragment
import com.konstantinisaev.youtrack.issuefilter.IssueSortFragment
import com.konstantinisaev.youtrack.ui.base.utils.Extra
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

    override fun showFilter(initialIssueCount: Int) {
        cicerone.router.navigateTo(object : SupportAppScreen(){
            override fun getFragment(): Fragment {
                return IssueFilterFragment().apply {
                    arguments = Bundle().apply { putInt(Extra.ISSUE_COUNT,initialIssueCount) }
                }
            }
        })
    }

    override fun showSort() {
        cicerone.router.navigateTo(object : SupportAppScreen(){
            override fun getFragment(): Fragment {
                return IssueSortFragment()            }
        })

    }

}