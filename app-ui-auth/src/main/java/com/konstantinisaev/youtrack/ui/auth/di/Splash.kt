package com.konstantinisaev.youtrack.ui.auth.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.konstantinisaev.youtrack.ui.auth.AuthFragment
import com.konstantinisaev.youtrack.ui.auth.CheckUrlFragment
import com.konstantinisaev.youtrack.ui.auth.SplashFragment
import com.konstantinisaev.youtrack.ui.auth.viewmodels.AuthByLoginPasswordViewModel
import com.konstantinisaev.youtrack.ui.auth.viewmodels.ServerConfigViewModel
import com.konstantinisaev.youtrack.ui.base.di.BaseModule
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewModelFactory
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewModelKey
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton


@Component(modules = [AuthViewModelsModule::class,BaseModule::class])
@Singleton
internal interface AuthComponent{

    fun injectFragment(fragment: SplashFragment)

    fun injectFragment(fragment: CheckUrlFragment)

    fun injectFragment(fragment: AuthFragment)

    @Component.Builder
    interface Builder {

        fun build(): AuthComponent

        @BindsInstance
        fun context(context: Context): Builder
    }
}

@Suppress("unused")
@Module
abstract class AuthViewModelsModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ServerConfigViewModel::class)
    internal abstract fun bindServerUrlViewModel(serverConfigViewModel: ServerConfigViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthByLoginPasswordViewModel::class)
    internal abstract fun bindAuthViewModel(serverConfigViewModel: AuthByLoginPasswordViewModel): ViewModel

}

internal class AuthDiProvider private constructor(){


    fun injectFragment(splashFragment: SplashFragment){
        authComponent.injectFragment(splashFragment)
    }

    fun injectFragment(checkUrlFragment: CheckUrlFragment){
        authComponent.injectFragment(checkUrlFragment)
    }

    fun injectFragment(authFragment: AuthFragment){
        authComponent.injectFragment(authFragment)
    }

    companion object {

        private lateinit var authComponent: AuthComponent

        private val splashDiProvider by lazy {
            AuthDiProvider()
        }

        fun getInstance(context: Context) : AuthDiProvider {
            if(!this::authComponent.isInitialized){
                authComponent = DaggerAuthComponent.builder().context(context).build()
            }
            return splashDiProvider
        }
    }



}

