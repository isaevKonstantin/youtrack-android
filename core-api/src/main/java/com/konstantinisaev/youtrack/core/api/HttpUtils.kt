package com.konstantinisaev.youtrack.core.api

import java.net.URL

object UrlFormatter {

    fun formatToHostUrls(baseUrl: String): List<String> {
        val url = URL(baseUrl)
        return listOf("https://${url.host}/${ApiEndpoints.YOUTRACK.url}", "https://${url.host}")
    }

    fun formatToServerConfigUrl(baseUrl: String) = "$baseUrl/${ApiEndpoints.SERVER_CONFIG.url}"
}