package com.konstantinisaev.youtrack.ui.auth.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.ui.auth.AuthFragment
import com.konstantinisaev.youtrack.ui.auth.CheckUrlFragment
import com.konstantinisaev.youtrack.ui.auth.SplashFragment
import com.konstantinisaev.youtrack.ui.auth.viewmodels.AuthByLoginPasswordViewModel
import com.konstantinisaev.youtrack.ui.auth.viewmodels.RefreshTokenViewModel
import com.konstantinisaev.youtrack.ui.auth.viewmodels.ServerConfigViewModel
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.utils.AuthRouter
import com.konstantinisaev.youtrack.ui.base.utils.Base64Converter
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewModelFactory
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewModelKey
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton


@Singleton
@Component
internal interface AuthComponent{

    fun injectFragment(fragment: SplashFragment)

    fun injectFragment(fragment: CheckUrlFragment)

    fun injectFragment(fragment: AuthFragment)

    @Component.Builder
    interface Builder {

        fun build(): AuthComponent

        @BindsInstance
        fun authRouter(authRouter: AuthRouter): Builder

        @BindsInstance
        fun apiProvider(apiProvider: ApiProvider) : Builder

        @BindsInstance
        fun preferenceAdapter(basePreferencesAdapter: BasePreferencesAdapter) : Builder

        @BindsInstance
        fun coroutineContextHolder(coroutineContextHolder: CoroutineContextHolder) : Builder

        @BindsInstance
        fun base64Converter(base64Converter: Base64Converter) : Builder

        @BindsInstance
        fun viewModelFactory(viewModelFactory: ViewModelProvider.Factory) : Builder
    }
}

@Suppress("unused")
@Module
abstract class AuthViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ServerConfigViewModel::class)
    internal abstract fun bindServerUrlViewModel(serverConfigViewModel: ServerConfigViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RefreshTokenViewModel::class)
    internal abstract fun bindRefreshTokenViewModel(refreshTokenViewModel: RefreshTokenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthByLoginPasswordViewModel::class)
    internal abstract fun bindAuthViewModel(serverConfigViewModel: AuthByLoginPasswordViewModel): ViewModel

}

class AuthDiProvider private constructor(){


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

        private val authDiProvider by lazy {
            AuthDiProvider()
        }

        fun init(authRouter: AuthRouter,apiProvider: ApiProvider,basePreferencesAdapter: BasePreferencesAdapter,coroutineContextHolder: CoroutineContextHolder,base64Converter: Base64Converter,viewModelFactory: ViewModelFactory){
            if(!this::authComponent.isInitialized){
                authComponent = DaggerAuthComponent.builder().authRouter(authRouter).
                    apiProvider(apiProvider).preferenceAdapter(basePreferencesAdapter).viewModelFactory(viewModelFactory).
                    coroutineContextHolder(coroutineContextHolder).base64Converter(base64Converter).build()
            }
        }


        fun getInstance() : AuthDiProvider {
            return authDiProvider
        }
    }
}

