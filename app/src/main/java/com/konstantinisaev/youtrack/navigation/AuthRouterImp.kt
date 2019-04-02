package com.konstantinisaev.youtrack.navigation

import androidx.fragment.app.Fragment
import com.konstantinisaev.youtrack.App
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.issuelist.IssueListContainerFragment
import com.konstantinisaev.youtrack.issuelist.di.IssueListDiProvider
import com.konstantinisaev.youtrack.ui.auth.AuthFragment
import com.konstantinisaev.youtrack.ui.auth.CheckUrlFragment
import com.konstantinisaev.youtrack.ui.auth.SplashFragment
import com.konstantinisaev.youtrack.ui.auth.di.AuthDiProvider
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.utils.AuthRouter
import com.konstantinisaev.youtrack.ui.base.utils.Base64Converter
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import javax.inject.Inject

class AuthRouterImp @Inject constructor(private val router: Router,private val basePreferencesAdapter: BasePreferencesAdapter,
                                        private val apiProvider: ApiProvider,private val base64Converter: Base64Converter,private val coroutineContextHolder: CoroutineContextHolder) : AuthRouter {

    init {
        IssueListDiProvider.init(App.context,apiProvider,basePreferencesAdapter,coroutineContextHolder)
        AuthDiProvider.init(App.context,apiProvider,basePreferencesAdapter,coroutineContextHolder,base64Converter)
    }

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