package com.konstantinisaev.youtrack.di

import android.content.Context
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.navigation.AuthRouterImp
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.utils.AuthRouter
import com.konstantinisaev.youtrack.ui.base.utils.Base64Converter
import com.konstantinisaev.youtrack.ui.base.utils.Base64ConverterImp
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideCicerone(): Cicerone<Router> = Cicerone.create()

    @Singleton
    @Provides
    fun provideRouter(cicerone: Cicerone<Router>) : Router = cicerone.router

    @Singleton
    @Provides
    fun provideSplashRouter(router: Router,base64Converter: Base64Converter,basePreferencesAdapter: BasePreferencesAdapter,apiProvider: ApiProvider,coroutineContextHolder: CoroutineContextHolder) : AuthRouter =
        AuthRouterImp(router,basePreferencesAdapter,apiProvider,base64Converter,coroutineContextHolder)

    @Singleton
    @Provides
    fun provideCoroutineContextHolder() = CoroutineContextHolder()

    @Provides
    @Singleton
    fun provideBasePreferenceAdapter(context: Context) = BasePreferencesAdapter.getInstance(context)

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