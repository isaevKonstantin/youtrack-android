package com.konstantinisaev.youtrack.navigation

import androidx.fragment.app.Fragment
import com.konstantinisaev.youtrack.ui.auth.CheckUrlFragment
import com.konstantinisaev.youtrack.ui.auth.SplashFragment
import com.konstantinisaev.youtrack.ui.base.utils.SplashRouter
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import javax.inject.Inject

class SplashRouterImp @Inject constructor(private val router: Router) : SplashRouter {

    override fun showServerUrl() {
        router.navigateTo(object : SupportAppScreen(){
            override fun getFragment(): Fragment {
                return CheckUrlFragment()
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