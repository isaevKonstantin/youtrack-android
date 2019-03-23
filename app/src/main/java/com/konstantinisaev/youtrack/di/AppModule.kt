package com.konstantinisaev.youtrack.di

import com.konstantinisaev.youtrack.navigation.AuthRouterImp
import com.konstantinisaev.youtrack.ui.base.utils.AuthRouter
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
    fun provideSplashRouter(router: Router) : AuthRouter = AuthRouterImp(router)


}