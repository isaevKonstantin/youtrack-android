package com.konstantinisaev.youtrack.navigation

import androidx.fragment.app.Fragment
import com.konstantinisaev.youtrack.issuelist.IssueListContainerFragment
import com.konstantinisaev.youtrack.ui.auth.AuthFragment
import com.konstantinisaev.youtrack.ui.auth.CheckUrlFragment
import com.konstantinisaev.youtrack.ui.auth.SplashFragment
import com.konstantinisaev.youtrack.ui.base.utils.AuthRouter
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import javax.inject.Inject

class AuthRouterImp @Inject constructor(private val router: Router) : AuthRouter {

    override fun showServerUrl() {
        router.replaceScreen(object : SupportAppScreen(){
            override fun getFragment(): Fragment {
                return CheckUrlFragment()
            }
        })
    }

    override fun showAuth() {
        router.navigateTo(object : SupportAppScreen(){
            override fun getFragment(): Fragment {
                return AuthFragment()
            }
        })
    }

    override fun showMain() {
        router.newRootScreen(object : SupportAppScreen(){
            override fun getFragment(): Fragment {
                return IssueListContainerFragment()
            }
        })

    }

    override fun showSplash() {
        router.navigateTo(object : SupportAppScreen(){
            override fun getFragment(): Fragment {
                return SplashFragment()
            }
        })
    }


}