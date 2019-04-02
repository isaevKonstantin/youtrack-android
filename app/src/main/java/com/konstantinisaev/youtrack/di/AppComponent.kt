package com.konstantinisaev.youtrack.di

import android.content.Context
import com.konstantinisaev.youtrack.EntryActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {

    fun injectEntryActivity(entryActivity: EntryActivity)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun context(context: Context): AppComponent.Builder

        @BindsInstance
        fun appModule(appModule: AppModule) : AppComponent.Builder

    }
}