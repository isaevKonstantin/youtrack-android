package com.konstantinisaev.youtrack.core.api

import java.net.URL

object UrlFormatter {

    fun formatToHostUrls(baseUrl: String): List<String> {
        val url = URL(baseUrl)
        return listOf("https://${url.host}/${ApiEndpoints.YOUTRACK.url}", "https://${url.host}")
    }

    fun formatToServerConfigUrl(baseUrl: String) = "$baseUrl/${ApiEndpoints.SERVER_CONFIG.url}"

    fun formatToLoginUrl(baseUrl: String, hubUrl: String): String{
        var url = baseUrl
        if(!url.endsWith("/")){
            url = "$url/"
        }
        if(url.endsWith("${ApiEndpoints.YOUTRACK.url}/")){
            url = url.replace("${ApiEndpoints.YOUTRACK.url}/","")
        }
        url = if(hubUrl.startsWith("/")){
            "$url${hubUrl.substring(1,hubUrl.length)}"
        }else{
            "$url$hubUrl"
        }
        return "$url/${ApiEndpoints.LOGIN.url}"
    }

}