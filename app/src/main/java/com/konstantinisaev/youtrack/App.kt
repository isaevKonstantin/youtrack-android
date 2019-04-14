package com.konstantinisaev.youtrack

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.di.AppComponent
import com.konstantinisaev.youtrack.di.DaggerAppComponent
import com.konstantinisaev.youtrack.issuefilter.di.IssueFilterDiProvider
import com.konstantinisaev.youtrack.issuelist.di.IssueListDiProvider
import com.konstantinisaev.youtrack.ui.auth.di.AuthDiProvider
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.utils.*
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewModelFactory
import javax.inject.Inject

class App : MultiDexApplication() {

    companion object {
        @JvmStatic lateinit var appComponent: AppComponent
        @SuppressLint("StaticFieldLeak")
        @JvmStatic lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        appComponent = DaggerAppComponent.builder().context(context).
            build()
        val appMediator = ApplicationMediator()
        appComponent.injectApplicationMediator(appMediator)
        appMediator.init(context)
        Settings.setDebugSettings(BuildConfig.DEBUG_SERVER_URL,BuildConfig.DEBUG_LOGIN,BuildConfig.DEBUG_PASSWORD)
    }
}

class ApplicationMediator {

    @Inject
    lateinit var mainRouter: MainRouter
    @Inject
    lateinit var authRouter: AuthRouter
    @Inject
    lateinit var issueFilterRouter: IssueFilterRouter
    @Inject
    lateinit var apiProvider: ApiProvider
    @Inject
    lateinit var coroutineContextHolder: CoroutineContextHolder
    @Inject
    lateinit var basePreferencesAdapter: BasePreferencesAdapter
    @Inject
    lateinit var base64Converter: Base64Converter
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var issueListRouter: IssueListRouter

    fun init(context: Context){
        AuthDiProvider.init(authRouter,apiProvider,basePreferencesAdapter,coroutineContextHolder,base64Converter,viewModelFactory)
        IssueListDiProvider.init(mainRouter,issueFilterRouter,issueListRouter,apiProvider,basePreferencesAdapter,coroutineContextHolder,viewModelFactory)
        IssueFilterDiProvider.init(apiProvider,basePreferencesAdapter,coroutineContextHolder,context,viewModelFactory)
    }

}