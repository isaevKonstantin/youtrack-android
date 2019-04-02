package com.konstantinisaev.youtrack

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.konstantinisaev.youtrack.di.AppComponent
import com.konstantinisaev.youtrack.di.AppModule
import com.konstantinisaev.youtrack.di.DaggerAppComponent
import com.konstantinisaev.youtrack.ui.base.utils.Settings

class App : MultiDexApplication() {

    companion object {
        @JvmStatic lateinit var appComponent: AppComponent
        @SuppressLint("StaticFieldLeak")
        @JvmStatic lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        appComponent = DaggerAppComponent.builder().context(context).
            appModule(AppModule()).build()
        Settings.setDebugSettings(BuildConfig.DEBUG_SERVER_URL,BuildConfig.DEBUG_LOGIN,BuildConfig.DEBUG_PASSWORD)
    }
}