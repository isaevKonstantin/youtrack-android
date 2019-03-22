package com.konstantinisaev.youtrack.ui.auth.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.konstantinisaev.youtrack.ui.auth.CheckUrlFragment
import com.konstantinisaev.youtrack.ui.auth.SplashFragment
import com.konstantinisaev.youtrack.ui.auth.viewmodels.ServerConfigViewModel
import com.konstantinisaev.youtrack.ui.base.di.BaseModule
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewModelFactory
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewModelKey
import dagger.*
import dagger.multibindings.IntoMap
import javax.inject.Singleton


@Component(modules = [SplashModule::class,SplashViewModelsModule::class,BaseModule::class])
@Singleton
internal interface SplashComponent{

    fun injectFragment(fragment: SplashFragment)

    fun injectFragment(fragment: CheckUrlFragment)

    @Component.Builder
    interface Builder {

        fun build(): SplashComponent

        @BindsInstance
        fun context(context: Context): Builder
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

internal class SplashDiProvider private constructor(){


    fun injectFragment(splashFragment: SplashFragment){
        splashComponent.injectFragment(splashFragment)
    }

    fun injectFragment(checkUrlFragment: CheckUrlFragment){
        splashComponent.injectFragment(checkUrlFragment)
    }

    companion object {

        private lateinit var splashComponent: SplashComponent

        private val splashDiProvider by lazy{
            SplashDiProvider()
        }

        fun getInstance(context: Context) : SplashDiProvider {
            splashComponent = DaggerSplashComponent.builder().context(context).build()
            return splashDiProvider
        }

    }



}

