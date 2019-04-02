package com.konstantinisaev.youtrack.ui.base.di

import androidx.lifecycle.ViewModelProvider
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewModelFactory
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class BaseModelsModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}