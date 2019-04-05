package com.konstantinisaev.youtrack.issuelist.di

import androidx.lifecycle.ViewModel
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.issuelist.IssueListContainerFragment
import com.konstantinisaev.youtrack.issuelist.IssueListFragment
import com.konstantinisaev.youtrack.issuelist.NavigationMenuFragment
import com.konstantinisaev.youtrack.issuelist.viewmodels.IssueCountViewModel
import com.konstantinisaev.youtrack.issuelist.viewmodels.IssueListTypeViewModel
import com.konstantinisaev.youtrack.issuelist.viewmodels.IssueListViewModel
import com.konstantinisaev.youtrack.issuelist.viewmodels.ProfileViewModel
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.di.BaseModelsModule
import com.konstantinisaev.youtrack.ui.base.utils.MainRouter
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewModelKey
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import javax.inject.Singleton


@Singleton
@Component(modules = [IssueListModelsModule::class, BaseModelsModule::class])
internal interface IssueListComponent{

    fun injectFragment(navigationMenuFragment: NavigationMenuFragment)

    fun injectFragment(issueListFragment: IssueListContainerFragment)

    fun injectFragment(issueListFragment: IssueListFragment)

    @Component.Builder
    interface Builder {

        fun build(): IssueListComponent

        @BindsInstance
        fun mainRouter(mainRouter: MainRouter): Builder

        @BindsInstance
        fun apiProvider(apiProvider: ApiProvider) : Builder

        @BindsInstance
        fun preferenceAdapter(basePreferencesAdapter: BasePreferencesAdapter) : Builder

        @BindsInstance
        fun coroutineContextHolder(coroutineContextHolder: CoroutineContextHolder) : Builder

        @BindsInstance
        fun cicerone(cicerone: Cicerone<Router>) : Builder
    }

}

@Suppress("unused")
@Module
abstract class IssueListModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IssueListViewModel::class)
    internal abstract fun bindIssueListViewModel(issueListViewModel: IssueListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IssueListTypeViewModel::class)
    internal abstract fun bindIssueFilterViewModel(issueListTypeViewModel: IssueListTypeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IssueCountViewModel::class)
    internal abstract fun bindIssueCountViewModel(issueCountViewModel: IssueCountViewModel): ViewModel

}

class IssueListDiProvider private constructor(){

    fun injectFragment(navigationMenuFragment: NavigationMenuFragment){
        component.injectFragment(navigationMenuFragment)
    }

    fun injectFragment(issueListFragment: IssueListContainerFragment){
        component.injectFragment(issueListFragment)
    }

    fun injectFragment(issueListFragment: IssueListFragment){
        component.injectFragment(issueListFragment)
    }

    companion object {

        private lateinit var component: IssueListComponent

        private val diProvider by lazy {
            IssueListDiProvider()
        }

        fun init(mainRouter: MainRouter,apiProvider: ApiProvider,basePreferencesAdapter: BasePreferencesAdapter,coroutineContextHolder: CoroutineContextHolder,cicerone: Cicerone<Router>){
            if(!this::component.isInitialized){
                component = DaggerIssueListComponent.builder().mainRouter(mainRouter).apiProvider(apiProvider).preferenceAdapter(basePreferencesAdapter).cicerone(cicerone).coroutineContextHolder(coroutineContextHolder).build()
            }
        }

        fun getInstance() : IssueListDiProvider {
            return diProvider
        }
    }
}


