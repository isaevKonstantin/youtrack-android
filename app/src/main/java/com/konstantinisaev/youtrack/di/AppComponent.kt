package com.konstantinisaev.youtrack.di

import android.content.Context
import com.konstantinisaev.youtrack.ApplicationMediator
import com.konstantinisaev.youtrack.EntryActivity
import com.konstantinisaev.youtrack.ui.base.di.BaseModelsModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class,AppViewModelModule::class,BaseModelsModule::class])
interface AppComponent {

    fun injectEntryActivity(entryActivity: EntryActivity)

    fun injectApplicationMediator(applicationMediator: ApplicationMediator)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun context(context: Context): AppComponent.Builder

    }
}