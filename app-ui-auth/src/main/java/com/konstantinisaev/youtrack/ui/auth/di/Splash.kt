package com.konstantinisaev.youtrack.ui.auth.di

import com.konstantinisaev.youtrack.ui.auth.SplashFragment
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Component(modules = [SplashModule::class])
@Singleton
internal interface SplashComponent{

    fun injectFragment(splashFragment: SplashFragment)

    @Component.Builder
    interface Builder {

        fun build(): SplashComponent

        fun splashModule(splashModule: SplashModule): Builder
    }
}

@Module
internal class SplashModule{

    @Provides
    fun provide() : String{ return ""}
}

internal object SplashDiProvider {

    private val splashComponent: SplashComponent by lazy {
        DaggerSplashComponent.builder().splashModule(SplashModule()).build()
    }

    fun injectFragment(splashFragment: SplashFragment) = splashComponent.injectFragment(splashFragment)


}

