package com.konstantinisaev.youtrack.ui.base.di

import android.content.Context
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BaseModule {

    @Singleton
    @Provides
    fun provideCoroutineContextHolder() = CoroutineContextHolder()

    @Provides
    @Singleton
    fun provideBasePreferenceAdapter(context: Context) = BasePreferencesAdapter(context)

    @Provides
    @Singleton
    fun provideApiProvider(preferencesAdapter: BasePreferencesAdapter) : ApiProvider {
        val apiProvider = ApiProvider()
        preferencesAdapter.getUrl()?.takeIf { it.isNotEmpty() }?.
            let { apiProvider.init(it,arrayOf()) }
        return apiProvider
    }

}