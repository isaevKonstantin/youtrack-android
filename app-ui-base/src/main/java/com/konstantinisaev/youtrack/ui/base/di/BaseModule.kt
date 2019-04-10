package com.konstantinisaev.youtrack.ui.base.di

import androidx.lifecycle.ViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.FilterUpdatedViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.IssueCountViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Suppress("unused")
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
}

