package com.konstantinisaev.youtrack.core.api

import kotlinx.coroutines.runBlocking
import okhttp3.logging.HttpLoggingInterceptor
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
            println(System.getenv())
            testUrl = System.getenv()[debugUrlKey] as String

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            apiProvider.init(
                testUrl,
                arrayOf(JsonInterceptor(), loggingInterceptor, serverCredentialsInterceptor)
            )
            runBlocking {
                getVersion()
                getServerConfig()
            }
        }

        private suspend fun getVersion() {
            val resp = apiProvider.getServerVersion("$testUrl${ApiEndpoints.YOUTRACK.url}/${ApiEndpoints.VERSION.url}")
                    .await()
            assertThat(resp.isSuccessful).isTrue()
            assertThat(resp.body()).isNotNull
        }

        private suspend fun getServerConfig() {
            val configResp = apiProvider.getServerConfig("$testUrl${ApiEndpoints.YOUTRACK.url}/${ApiEndpoints.CONFIG.url}").await()
            assertThat(configResp.isSuccessful).isTrue()
            assertThat(configResp.body()).isNotNull
            respServerConfig = configResp.body()!!
        }
    }
}