package com.konstantinisaev.youtrack.core.api

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.BeforeClass
import org.junit.Test
import java.util.*

class ApiProviderTest {

    @Test
    fun `should be initialized`() {
        assertThat(serverConfigDTO.mobile).isNotNull
        assertThat(serverConfigDTO.ring).isNotNull
        assertThat(serverConfigDTO.version).isNotEmpty()
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

    @Test
    fun `should return not empty filter auto complete list`() {
        runBlocking {
            apiProvider.enableUserCredentialsInHeader(authTokenDTO.accessToken, authTokenDTO.tokenType)
            val resp = apiProvider.getIssuesFilterIntellisense("$testUrl${ApiEndpoints.YOUTRACK.url}/").await()
            assertThat(resp).isNotNull
        }
    }

    @Test
    fun `should create issue`() {
        runBlocking {
            apiProvider.enableUserCredentialsInHeader(authTokenDTO.accessToken, authTokenDTO.tokenType)
            println(projects)
            val issueDTO = apiProvider.initDraft("$testUrl${ApiEndpoints.YOUTRACK.url}/", projects.firstOrNull{ it.shortName?.toLowerCase() == "ya" }?.id.orEmpty()).await()
            assertThat(issueDTO).isNotNull
            val draftIssueDTO = apiProvider.getIssueByDraftId("$testUrl${ApiEndpoints.YOUTRACK.url}/",issueDTO.id.orEmpty()).await()
            assertThat(draftIssueDTO).isNotNull
            draftIssueDTO.fields?.forEach {fieldContainer ->
                println(fieldContainer)
                fieldContainer.projectCustomField?.bundle.takeIf { it != null }?.let {bundle ->
                    val fieldDTO = apiProvider.getCustomFieldSettings(
                        "$testUrl${ApiEndpoints.YOUTRACK.url}/",
                        fieldContainer.projectCustomField?.field?.fieldType?.valueType.orEmpty(),
                        bundle.id.orEmpty()
                    ).await()
                    assertThat(fieldDTO).isNotEmpty
                }
            }


        }
    }

    companion object {

        private val apiProvider = ApiProvider()
        private lateinit var serverConfigDTO: ServerConfigDTO
        private lateinit var authTokenDTO: AuthTokenDTO
        private lateinit var projects: List<ProjectDTO>
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
                getServerConfig()
                apiProvider.enableAppCredentialsInHeader(Base64.getEncoder().encodeToString("${serverConfigDTO.mobile.serviceId}:${serverConfigDTO.mobile.serviceSecret}".toByteArray()))
                getToken(System.getenv()[debugLoginKey] as String,System.getenv()[debugPasswordKey] as String,"${serverConfigDTO.mobile.serviceId} ${serverConfigDTO.ring.serviceId}")
                getProjects()
            }
        }

        private suspend fun getToken(login: String, password: String, serviceId: String) {
            var hub = serverConfigDTO.ring.url
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
            serverConfigDTO = configDTO
        }

        private suspend fun getProjects(){
            apiProvider.enableUserCredentialsInHeader(authTokenDTO.accessToken, authTokenDTO.tokenType)
            val resp = apiProvider.getProjects("$testUrl${ApiEndpoints.YOUTRACK.url}/").await()
            assertThat(resp).isNotEmpty
            projects = resp
        }

    }
}