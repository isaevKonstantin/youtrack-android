package com.konstantinisaev.youtrack.ui.base.extensions

import androidx.appcompat.app.AppCompatActivity
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

fun Cicerone<Router>.initRouter(activity: AppCompatActivity,containerId: Int){
    this.navigatorHolder.setNavigator(
        ru.terrakok.cicerone.android.support.SupportAppNavigator(
            activity,
            containerId
        )
    )

}