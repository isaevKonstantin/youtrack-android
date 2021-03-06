@file:Suppress("unused")

package com.konstantinisaev.youtrack.ui.base.di

import androidx.lifecycle.ViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Named
import javax.inject.Singleton

@Module
abstract class BaseModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(IssueCountViewModel::class)
    internal abstract fun bindIssueCountViewModel(issueCountViewModel: IssueCountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FilterUpdatedViewModel::class)
    @Singleton
    internal abstract fun bindFilterUpdatedViewModel(filterUpdatedViewModel: FilterUpdatedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UpdateIssueListViewModel::class)
    @Singleton
    internal abstract fun bindUpdateIssuesViewModel(updateIssueListViewModel: UpdateIssueListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GetProjectsViewModel::class)
    @Singleton
    internal abstract fun bindGetProjectsViewModel(getProjectsViewModel: GetProjectsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GetPermissionsViewModel::class)
    @Singleton
    internal abstract fun bindGetPermissionsViewModel(getPermissionsViewModel: GetPermissionsViewModel): ViewModel

}

@Module
abstract class FeatureViewModelFactoryModule {

    @Binds
    @Named("featureFactory")
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelFactory
}

