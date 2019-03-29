package com.konstantinisaev.youtrack.core.api

import java.io.IOException

sealed class HttpProtocolException : IOException(){

    class Unknown() : HttpProtocolException()
    class Unauthorized(override val message: String) : HttpProtocolException()
    class Forbidden(override val message: String) : HttpProtocolException()
    class BadRequest(override val message: String) : HttpProtocolException()
}