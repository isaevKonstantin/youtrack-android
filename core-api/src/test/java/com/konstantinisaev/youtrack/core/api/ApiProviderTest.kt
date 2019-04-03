package com.konstantinisaev.youtrack.core.api

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.BeforeClass
import org.junit.Test
import java.util.*

class ApiProviderTest {

    @Test
    fun `should be initialized`() {
        assertThat(respServerConfig.mobile).isNotNull
        assertThat(respServerConfig.ring).isNotNull
        assertThat(respServerConfig.version).isNotEmpty()
        assertThat(authTokenDTO.accessToken).isNotEmpty()
    }

    @Test
    fun `should return not empty profile`() {
        runBlocking {
            apiProvider.enableUserCredentialsInHeader(authTokenDTO.accessToken, authTokenDTO.tokenType)
            val resp = apiProvider.getProfile("$testUrl${ApiEndpoints.YOUTRACK.url}/").await()
            assertThat(resp.email).isNotEmpty()
        }
    }

    @Test
    fun `should return not empty project list`() {
        runBlocking {
            apiProvider.enableUserCredentialsInHeader(authTokenDTO.accessToken, authTokenDTO.tokenType)
            val resp = apiProvider.getProjects("$testUrl${ApiEndpoints.YOUTRACK.url}/").await()
            assertThat(resp).isNotEmpty
        }
    }

    @Test
    fun `should return not empty issue list`() {
        runBlocking {
            apiProvider.enableUserCredentialsInHeader(authTokenDTO.accessToken, authTokenDTO.tokenType)
            val resp = apiProvider.getAllIssues("$testUrl${ApiEndpoints.YOUTRACK.url}/").await()
            assertThat(resp).isNotEmpty
        }
    }

    @Test
    fun `should return not empty issue count`() {
        runBlocking {
            apiProvider.enableUserCredentialsInHeader(authTokenDTO.accessToken, authTokenDTO.tokenType)
            val resp = apiProvider.getAllIssuesCount("$testUrl${ApiEndpoints.YOUTRACK.url}/").await()
            assertThat(resp).isNotNull
        }
    }

    companion object {

        private val apiProvider = ApiProvider()
        private lateinit var respServerConfig: ServerConfigDTO
        private lateinit var authTokenDTO: AuthTokenDTO
        private lateinit var testUrl: String

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
                apiProvider.enableAppCredentialsInHeader(Base64.getEncoder().encodeToString("${respServerConfig.mobile.serviceId}:${respServerConfig.mobile.serviceSecret}".toByteArray()))
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
            val loginUrl = UrlFormatter.formatToLoginUrl(testUrl,hub)
            val authTokenDto = apiProvider.login(loginUrl,login,password,serviceId)
                    .await()
            assertThat(authTokenDto).isNotNull
            this.authTokenDTO = authTokenDto
            val newAuthTokenDTO = apiProvider.refreshToken(loginUrl,authTokenDto.refreshToken).await()
            assertThat(newAuthTokenDTO).isNotNull
            this.authTokenDTO = newAuthTokenDTO
        }

        private suspend fun getServerConfig() {
            val configDTO = apiProvider.getServerConfig("$testUrl${ApiEndpoints.YOUTRACK.url}/${ApiEndpoints.CONFIG.url}").await()
            assertThat(configDTO).isNotNull
            respServerConfig = configDTO
        }
    }
}