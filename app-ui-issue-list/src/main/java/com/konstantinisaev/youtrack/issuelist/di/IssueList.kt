package com.konstantinisaev.youtrack.issuelist.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.issuelist.NavigationMenuFragment
import com.konstantinisaev.youtrack.issuelist.ProfileViewModel
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.di.BaseModelsModule
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewModelKey
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton


@Singleton
@Component(modules = [IssueListModelsModule::class, BaseModelsModule::class])
internal interface IssueListComponent{

    fun injectFragment(navigationMenuFragment: NavigationMenuFragment)

    @Component.Builder
    interface Builder {

        fun build(): IssueListComponent

        @BindsInstance
        fun context(context: Context): Builder

        @BindsInstance
        fun apiProvider(apiProvider: ApiProvider) : Builder

        @BindsInstance
        fun preferenceAdapter(basePreferencesAdapter: BasePreferencesAdapter) : Builder

        @BindsInstance
        fun coroutineContextHolder(coroutineContextHolder: CoroutineContextHolder) : Builder
    }

}

@Suppress("unused")
@Module
abstract class IssueListModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun bindServerUrlViewModel(profileViewModel: ProfileViewModel): ViewModel

}

class IssueListDiProvider private constructor(){

    fun injectFragment(navigationMenuFragment: NavigationMenuFragment){
        component.injectFragment(navigationMenuFragment)
    }

    companion object {

        private lateinit var component: IssueListComponent

        private val diProvider by lazy {
            IssueListDiProvider()
        }

        fun init(context: Context,apiProvider: ApiProvider,basePreferencesAdapter: BasePreferencesAdapter,coroutineContextHolder: CoroutineContextHolder){
            if(!this::component.isInitialized){
                component = DaggerIssueListComponent.builder().context(context).apiProvider(apiProvider).preferenceAdapter(basePreferencesAdapter).coroutineContextHolder(coroutineContextHolder).build()
            }
        }

        fun getInstance() : IssueListDiProvider {
            return diProvider
        }
    }
}


