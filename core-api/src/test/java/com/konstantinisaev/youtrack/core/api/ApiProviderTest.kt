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
    }

    companion object {

        private val apiProvider = ApiProvider()
        private lateinit var respServerConfig: ServerConfigDTO
        private lateinit var testUrl: String
        private val serverCredentialsInterceptor = ServerCredentialsInterceptor()

        @BeforeClass
        @JvmStatic
        fun setup() {
            val debugUrlKey = "debug_url"
            assertThat(System.getenv().containsKey(debugUrlKey)).isTrue()
            testUrl = System.getenv()[debugUrlKey] as String
            println(testUrl)

            apiProvider.init(
                testUrl,
                arrayOf()
            )
            runBlocking {
                getVersion()
                getServerConfig()
            }
        }

        private suspend fun getVersion() {
            val versionDTO = apiProvider.getServerVersion("$testUrl${ApiEndpoints.YOUTRACK.url}/${ApiEndpoints.VERSION.url}")
                    .await()
            assertThat(versionDTO).isNotNull
        }

        private suspend fun getServerConfig() {
            val configDTO = apiProvider.getServerConfig("$testUrl${ApiEndpoints.YOUTRACK.url}/${ApiEndpoints.CONFIG.url}").await()
            assertThat(configDTO).isNotNull
            respServerConfig = configDTO
        }
    }
}