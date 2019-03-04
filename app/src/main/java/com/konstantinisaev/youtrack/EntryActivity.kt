package com.konstantinisaev.youtrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.konstantinisaev.youtrack.di.AppComponent
import com.konstantinisaev.youtrack.ui.auth.SplashFragment
import com.konstantinisaev.youtrack.ui.base.BaseActivity
import com.konstantinisaev.youtrack.ui.base.extensions.initRouter
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import javax.inject.Inject

class EntryActivity : BaseActivity() {

    @Inject
    lateinit var cicerone: Cicerone<Router>
    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)
        App.appComponent?.injectEntryActivity(this)
        cicerone.navigatorHolder.setNavigator(SupportAppNavigator(this,R.id.flContainer))
        cicerone.router.navigateTo(object : SupportAppScreen(){
            override fun getFragment(): Fragment {
                return SplashFragment()
            }
        })
    }
}