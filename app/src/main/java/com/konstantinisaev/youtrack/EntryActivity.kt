package com.konstantinisaev.youtrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.konstantinisaev.youtrack.ui.auth.SplashFragment
import com.konstantinisaev.youtrack.ui.base.BaseActivity
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen

class EntryActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)
        val cicerone = Cicerone.create()
        val navigator = SupportAppNavigator(this,R.id.flContainer)
        cicerone.navigatorHolder.setNavigator(navigator)
        cicerone.router.navigateTo(object : SupportAppScreen(){
            override fun getFragment(): Fragment {
                return SplashFragment()
            }
        })
    }
}