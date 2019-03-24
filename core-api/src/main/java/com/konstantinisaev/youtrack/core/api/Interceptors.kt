package com.konstantinisaev.youtrack.core.api

import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class JsonInterceptor : Interceptor {
	override fun intercept(chain: Interceptor.Chain?): Response {
		val requestBuilder = chain!!.request().newBuilder()
		requestBuilder.header("Accept", "application/json")
		requestBuilder.header("Content-Type", "application/x-www-form-urlencoded")
		return chain.proceed(requestBuilder.build())
	}
}

class ServerCredentialsInterceptor : Interceptor {

	private var serverCredentials: Credentials.ServerCredentials? = null
	private var userCredentials: Credentials.UserCredentials? = null

	fun initServiceCredentials(clientId: String, clientSecret: String){
		serverCredentials = Credentials.ServerCredentials(clientSecret,clientId)
	}

	fun clearServiceCredentials(){
		serverCredentials = null
	}

	fun initUserCredentials(accessToken: String,tokenType: String){
		userCredentials = Credentials.UserCredentials(accessToken,tokenType)
	}

	override fun intercept(chain: Interceptor.Chain?): Response {
		val requestBuilder = chain!!.request().newBuilder()
		if (serverCredentials != null) {
			val decodedStr = Base64.getEncoder().encodeToString("${serverCredentials!!.clientId}:${serverCredentials!!.clientSecret}".toByteArray())
			requestBuilder.header(AUTHORIZATION_HEADER, "Basic $decodedStr")
		}
		if (userCredentials != null) {
			requestBuilder.header(
				AUTHORIZATION_HEADER,
				"${userCredentials?.tokenType} ${userCredentials?.accessToken}"
			)
		}
		return chain.proceed(requestBuilder.build())
	}
}

private sealed class Credentials{

	class ServerCredentials(val clientSecret: String = "",val clientId: String = "") : Credentials()
	class UserCredentials(val accessToken: String = "",val tokenType: String = "") : Credentials()
}

private const val AUTHORIZATION_HEADER = "Authorization"