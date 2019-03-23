package com.konstantinisaev.youtrack.ui.base.utils

object Settings {

    var debugUrl= ""
    var debugLogin = ""
    var debugPassword = ""

    fun setDebugSettings(url: String, login: String, password: String){
        debugUrl = url
        debugLogin = login
        debugPassword = password
    }
}