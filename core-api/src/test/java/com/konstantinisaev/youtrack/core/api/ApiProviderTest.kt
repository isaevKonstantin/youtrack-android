package com.konstantinisaev.youtrack.core.api

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.BeforeClass
import org.junit.Test

class ApiProviderTest {

    @Test
    fun initializationTest() {
        assertThat(respServerConfig.mobile).isNotNull
        assertThat(respServerConfig.ring).isNotNull
        assertThat(respServerConfig.version).isNotEmpty()
        assertThat(authTokenDTO.accessToken).isNotEmpty()
    }

    companion object {

        private val apiProvider = ApiProvider()
        private lateinit var respServerConfig: ServerConfigDTO
        private lateinit var authTokenDTO: AuthTokenDTO
        private lateinit var testUrl: String
        private val serverCredentialsInterceptor = ServerCredentialsInterceptor()

        @BeforeClass
        @JvmStatic
        fun setup() {
            val debugUrlKey = "debug_url"
            val debugLoginKey = "debug_login"
            val debugPasswordKey = "debug_password"
            assertThat(System.getenv().containsKey(debugUrlKey)).isTrue()
            assertThat(System.getenv().containsKey(debugLoginKey)).isTrue()
            assertThat(System.getenv().containsKey(debugPasswordKey)).isTrue()

            testUrl = System.getenv()[debugUrlKey] as String
            println(testUrl)

            apiProvider.init(
                testUrl,
                arrayOf()
            )
            runBlocking {
                getVersion()
                getServerConfig()
                apiProvider.enableAppCredentialsInHeader(respServerConfig.mobile.serviceId, respServerConfig.mobile.serviceSecret)
                getToken(System.getenv()[debugLoginKey] as String,System.getenv()[debugPasswordKey] as String,"${respServerConfig.mobile.serviceId} ${respServerConfig.ring.serviceId}")
            }
        }

        private suspend fun getVersion() {
            val versionDTO = apiProvider.getServerVersion("$testUrl${ApiEndpoints.YOUTRACK.url}/${ApiEndpoints.VERSION.url}")
                    .await()
            assertThat(versionDTO).isNotNull
        }

        private suspend fun getToken(login: String, password: String, serviceId: String) {
            var hub = respServerConfig.ring.url
            if(hub.startsWith("/")){
                hub = hub.substring(1,hub.length)
            }

            val authTokenDto = apiProvider.login("$testUrl${hub}/${ApiEndpoints.LOGIN.url}",login,password,serviceId)
                    .await()
            assertThat(authTokenDto).isNotNull
            this.authTokenDTO = authTokenDto
        }

        private suspend fun getServerConfig() {
            val configDTO = apiProvider.getServerConfig("$testUrl${ApiEndpoints.YOUTRACK.url}/${ApiEndpoints.CONFIG.url}").await()
            assertThat(configDTO).isNotNull
            respServerConfig = configDTO
        }
    }
}