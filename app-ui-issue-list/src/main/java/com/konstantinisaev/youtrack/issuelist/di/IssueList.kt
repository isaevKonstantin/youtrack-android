package com.konstantinisaev.youtrack.issuelist.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.issuelist.IssueListContainerFragment
import com.konstantinisaev.youtrack.issuelist.IssueListFragment
import com.konstantinisaev.youtrack.issuelist.NavigationMenuFragment
import com.konstantinisaev.youtrack.issuelist.viewmodels.IssueListTypeViewModel
import com.konstantinisaev.youtrack.issuelist.viewmodels.IssueListViewModel
import com.konstantinisaev.youtrack.issuelist.viewmodels.IssueSavedFilterViewModel
import com.konstantinisaev.youtrack.issuelist.viewmodels.ProfileViewModel
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.utils.IssueListRouter
import com.konstantinisaev.youtrack.ui.base.utils.MainRouter
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
        fun issueListRouter(issueListRouter: IssueListRouter): Builder

        @BindsInstance
        fun apiProvider(apiProvider: ApiProvider) : Builder

        @BindsInstance
        fun preferenceAdapter(basePreferencesAdapter: BasePreferencesAdapter) : Builder

        @BindsInstance
        fun coroutineContextHolder(coroutineContextHolder: CoroutineContextHolder) : Builder

        @BindsInstance
        fun viewModelFactory(viewModelFactory: ViewModelProvider.Factory) : Builder

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
    internal abstract fun bindIssueListTypeViewModel(issueListTypeViewModel: IssueListTypeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IssueSavedFilterViewModel::class)
    internal abstract fun bindIssueFilterViewModel(issueSavedFilterViewModel: IssueSavedFilterViewModel): ViewModel

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

        fun init(
            mainRouter: MainRouter,
            issueListRouter: IssueListRouter,
            apiProvider: ApiProvider,
            basePreferencesAdapter: BasePreferencesAdapter,
            coroutineContextHolder: CoroutineContextHolder,
            viewModelFactory: ViewModelFactory
        ){
            if(!this::component.isInitialized){
                component = DaggerIssueListComponent.builder().mainRouter(mainRouter).apiProvider(apiProvider).issueListRouter(issueListRouter).
                    preferenceAdapter(basePreferencesAdapter).viewModelFactory(viewModelFactory).
                    coroutineContextHolder(coroutineContextHolder).build()
            }
        }

        fun getInstance() : IssueListDiProvider {
            return diProvider
        }
    }
}


