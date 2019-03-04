package com.konstantinisaev.youtrack

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.konstantinisaev.youtrack.di.AppComponent
import com.konstantinisaev.youtrack.di.AppModule
import com.konstantinisaev.youtrack.di.DaggerAppComponent

class App : MultiDexApplication() {

    companion object {
        @JvmStatic var appComponent: AppComponent? = null
        @SuppressLint("StaticFieldLeak")
        @JvmStatic lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        if(appComponent==null){
            appComponent = DaggerAppComponent.builder().
                appModule(AppModule(this)).build()
        }
    }
}