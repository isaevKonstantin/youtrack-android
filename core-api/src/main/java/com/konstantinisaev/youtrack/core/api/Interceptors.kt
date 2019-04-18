package com.konstantinisaev.youtrack.core.api

import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection

class JsonInterceptor : Interceptor {
	override fun intercept(chain: Interceptor.Chain?): Response {
		val requestBuilder = chain!!.request().newBuilder()
		requestBuilder.header("Accept", "application/json, text/plain, */*")
		requestBuilder.header("Content-Type", "application/x-www-form-urlencoded")
		return chain.proceed(requestBuilder.build())
	}
}

class ServerCredentialsInterceptor : Interceptor {

	private var serverCredentials: Credentials.ServerCredentials? = null
	private var userCredentials: Credentials.UserCredentials? = null

	fun initServiceCredentials(base64Credentials: String){
		serverCredentials = Credentials.ServerCredentials(base64Credentials)
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
			val decodedStr = serverCredentials!!.base64Credentials
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

class ErrorInterceptor : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		val response = chain.proceed(request)
		when {
			response.code() == HttpURLConnection.HTTP_UNAUTHORIZED -> throw HttpProtocolException.Unauthorized(response.message())
			response.code() == HttpURLConnection.HTTP_FORBIDDEN -> throw HttpProtocolException.Forbidden(response.message())
			response.code() == HttpURLConnection.HTTP_BAD_REQUEST -> throw HttpProtocolException.BadRequest(response.message())
			else -> return response
		}
	}
}

private sealed class Credentials{

	class ServerCredentials(val base64Credentials: String) : Credentials()
	class UserCredentials(val accessToken: String = "",val tokenType: String = "") : Credentials()
}

private const val AUTHORIZATION_HEADER = "Authorization"