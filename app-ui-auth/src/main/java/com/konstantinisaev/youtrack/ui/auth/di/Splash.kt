package com.konstantinisaev.youtrack.ui.auth.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.konstantinisaev.youtrack.ui.auth.SplashFragment
import com.konstantinisaev.youtrack.ui.auth.viewmodels.ServerConfigViewModel
import com.konstantinisaev.youtrack.ui.base.ViewModelFactory
import com.konstantinisaev.youtrack.ui.base.ViewModelKey
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton


@Component(modules = [SplashModule::class,SplashViewModelsModule::class])
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
internal class SplashModule {

    @Provides
    fun provide() : String{ return ""}
}

@Suppress("unused")
@Module
abstract class SplashViewModelsModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ServerConfigViewModel::class)
    internal abstract fun bindServerUrlViewModel(serverConfigViewModel: ServerConfigViewModel): ViewModel

}

internal object SplashDiProvider {

    private val splashComponent: SplashComponent by lazy {
        DaggerSplashComponent.builder().splashModule(SplashModule()).build()
    }

    fun injectFragment(splashFragment: SplashFragment) = splashComponent.injectFragment(splashFragment)


}

