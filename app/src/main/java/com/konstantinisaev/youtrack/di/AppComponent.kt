package com.konstantinisaev.youtrack.di

import com.konstantinisaev.youtrack.EntryActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {

    fun injectEntryActivity(entryActivity: EntryActivity)
}