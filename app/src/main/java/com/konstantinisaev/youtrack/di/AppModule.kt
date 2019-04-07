package com.konstantinisaev.youtrack.di

import com.konstantinisaev.youtrack.App.Companion.context
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.navigation.AuthRouterImp
import com.konstantinisaev.youtrack.navigation.IssueListRouterImp
import com.konstantinisaev.youtrack.navigation.MainRouterImp
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.utils.*
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
    fun provideAuthRouter(cicerone: Cicerone<Router>, base64Converter: Base64Converter, basePreferencesAdapter: BasePreferencesAdapter, apiProvider: ApiProvider, coroutineContextHolder: CoroutineContextHolder) : AuthRouter =
        AuthRouterImp(cicerone,basePreferencesAdapter,apiProvider,base64Converter,coroutineContextHolder)

    @Singleton
    @Provides
    fun provideMainRouter(cicerone: Cicerone<Router>, basePreferencesAdapter: BasePreferencesAdapter, apiProvider: ApiProvider, coroutineContextHolder: CoroutineContextHolder) : MainRouter =
        MainRouterImp(cicerone,basePreferencesAdapter,apiProvider,coroutineContextHolder)

    @Singleton
    @Provides
    fun provideIssueListRouter(cicerone: Cicerone<Router>, basePreferencesAdapter: BasePreferencesAdapter, apiProvider: ApiProvider, coroutineContextHolder: CoroutineContextHolder) : IssueListRouter =
        IssueListRouterImp(cicerone,basePreferencesAdapter,apiProvider,coroutineContextHolder)

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