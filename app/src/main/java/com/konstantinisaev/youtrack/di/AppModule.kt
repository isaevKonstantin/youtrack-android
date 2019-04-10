package com.konstantinisaev.youtrack.di

import androidx.lifecycle.ViewModelProvider
import com.konstantinisaev.youtrack.App.Companion.context
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.issuefilter.di.IssueFilterModule
import com.konstantinisaev.youtrack.issuefilter.di.IssueFilterViewModelModule
import com.konstantinisaev.youtrack.issuelist.di.IssueListModelsModule
import com.konstantinisaev.youtrack.navigation.AuthRouterImp
import com.konstantinisaev.youtrack.navigation.IssueListRouterImp
import com.konstantinisaev.youtrack.navigation.MainRouterImp
import com.konstantinisaev.youtrack.ui.auth.di.AuthViewModelsModule
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.di.BaseModelsModule
import com.konstantinisaev.youtrack.ui.base.utils.*
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideCicerone(): Cicerone<Router> = Cicerone.create()

    @Singleton
    @Provides
    fun provideAuthRouter(cicerone: Cicerone<Router>) : AuthRouter =
        AuthRouterImp(cicerone)

    @Singleton
    @Provides
    fun provideMainRouter(cicerone: Cicerone<Router>) : MainRouter =
        MainRouterImp(cicerone)

    @Singleton
    @Provides
    fun provideIssueListRouter(cicerone: Cicerone<Router>) : IssueListRouter =
        IssueListRouterImp(cicerone)

    @Singleton
    @Provides
    fun provideCoroutineContextHolder() = CoroutineContextHolder()

    @Provides
    @Singleton
    fun provideBasePreferenceAdapter() = BasePreferencesAdapter.getInstance(context)

    @Provides
    @Singleton
    fun provideBase64Converter() : Base64Converter = Base64ConverterImp()

    @Provides
    @Singleton
    fun provideApiProvider(preferencesAdapter: BasePreferencesAdapter) : ApiProvider {
        val apiProvider = ApiProvider()
        preferencesAdapter.getUrl().takeIf { it.isNotEmpty() }?.
            let { apiProvider.init(it,arrayOf()) }
        return apiProvider
    }

}

@Module(includes = [BaseModelsModule::class,AuthViewModelsModule::class,IssueListModelsModule::class,IssueFilterViewModelModule::class,IssueFilterModule::class])
abstract class AppViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}