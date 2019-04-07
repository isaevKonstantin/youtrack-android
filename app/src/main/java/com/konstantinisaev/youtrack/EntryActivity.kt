package com.konstantinisaev.youtrack

import android.os.Bundle
import com.konstantinisaev.youtrack.ui.base.screens.BaseActivity
import com.konstantinisaev.youtrack.ui.base.utils.AuthRouter
import com.konstantinisaev.youtrack.ui.base.utils.MainRouter
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Inject

class EntryActivity : BaseActivity() {

    @Inject
    lateinit var authRouter: AuthRouter

    @Inject
    lateinit var mainRouter: MainRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)
        App.appComponent.injectEntryActivity(this)
        authRouter.setNavigator(SupportAppNavigator(this,R.id.flContainer))
        authRouter.showSplash()
    }
}