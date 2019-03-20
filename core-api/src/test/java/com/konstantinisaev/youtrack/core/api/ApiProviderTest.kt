package com.konstantinisaev.youtrack.core.api

import kotlinx.coroutines.runBlocking
import okhttp3.logging.HttpLoggingInterceptor
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test

class ApiProviderTest {

    @Test
    fun initializationTest() {
        println(respServerConfig)
    }


    companion object {

        private val apiProvider = ApiProvider()
        private lateinit var respServerConfig: ServerConfigDTO
        private lateinit var testUrl: String
        private val serverCredentialsInterceptor = ServerCredentialsInterceptor()

        @BeforeClass
        @JvmStatic
        fun setup() {

            assertTrue(System.getenv().containsKey("debug_url"))
            println(System.getenv())
            testUrl = System.getenv()["debug_url"] as String

            println(testUrl)
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
            val resp = apiProvider.getServerVersion("$testUrl${ApiEndpoints.YOUTRACK.url}/rest/workflow/version")
                    .await()
            assertThat(resp.isSuccessful).isTrue()
            assertThat(resp.body()).isNotNull
        }

        private suspend fun getServerConfig() {
            val configResp = apiProvider.getServerConfig("${testUrl}youtrack/api/config").await()
            assertTrue(configResp.isSuccessful)
            assertThat(configResp.isSuccessful).isTrue()
            assertThat(configResp.body()?.mobile).isNotNull
            assertThat(configResp.body()?.ring).isNotNull
            assertThat(configResp.body()?.version).isNotEmpty()
            respServerConfig = configResp.body()!!
        }
    }
}