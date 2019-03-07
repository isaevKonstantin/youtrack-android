package com.konstantinisaev.youtrack.ui.base.di

import android.app.Application
import android.content.Context
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BaseModule {

    @Provides
    @Singleton
    fun provideBasePreferenceAdapter(context: Context) = BasePreferencesAdapter(context)

}