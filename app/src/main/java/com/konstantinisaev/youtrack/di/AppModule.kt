package com.konstantinisaev.youtrack.di

import com.konstantinisaev.youtrack.App
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {

    @Singleton
    @Provides
    fun provideCicerone(): Cicerone<Router> = Cicerone.create()

    @Singleton
    @Provides
    fun provideRouter(cicerone: Cicerone<Router>) : Router {
        return cicerone.router
    }


}