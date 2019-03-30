package com.konstantinisaev.youtrack.ui.auth.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.konstantinisaev.youtrack.core.api.*
import com.konstantinisaev.youtrack.ui.auth.testCoroutineContextHolder
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.utils.Base64Converter
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
@Suppress("DeferredResultUnused")
class RefreshTokenViewModelTest {

    private lateinit var refreshTokenViewModel: BaseViewModel<String>

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiProvider: ApiProvider
    @Mock
    private lateinit var basePreferencesAdapter: BasePreferencesAdapter
    @Mock
    private lateinit var base64Converter: Base64Converter

    private val serverConfigDTO = ServerConfigDTO(
        "version",
        MobileConfigDTO("secret", "id", "type"),
        RingConfigDTO("id", "url", "type"),
        false,
        "type"
    )

    private val authTokenDTO = AuthTokenDTO("","",1,"","")


    @Before
    fun setUp() {
        refreshTokenViewModel = RefreshTokenViewModel(apiProvider,basePreferencesAdapter, base64Converter,
            testCoroutineContextHolder
        )
    }

    @Test
    fun `given null config should produce empty state`() {
        Mockito.`when`(basePreferencesAdapter.getServerConfig()).thenReturn(null)
        refreshTokenViewModel.doAsyncRequest(params = "")
        Assertions.assertThat(refreshTokenViewModel.lastViewState).isInstanceOf(ViewState.Empty::class.java)
    }

    @Test
    fun `given null auth token should produce empty state`() {
        Mockito.`when`(basePreferencesAdapter.getAuthToken()).thenReturn(null)
        refreshTokenViewModel.doAsyncRequest(params = "")
        Assertions.assertThat(refreshTokenViewModel.lastViewState).isInstanceOf(ViewState.Empty::class.java)
    }

    @Test
    fun `given error config should produce error state`() {
        Mockito.`when`(basePreferencesAdapter.getServerConfig()).thenReturn(serverConfigDTO)
        Mockito.`when`(basePreferencesAdapter.getAuthToken()).thenReturn(authTokenDTO)
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(apiProvider.refreshToken(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenThrow(RuntimeException("test"))
        refreshTokenViewModel.doAsyncRequest(params = "")
        Assertions.assertThat(refreshTokenViewModel.lastViewState).isInstanceOf(ViewState.Error::class.java)
    }

    @Test
    fun `given valid config should produce success state`() {
        Mockito.`when`(basePreferencesAdapter.getServerConfig()).thenReturn(serverConfigDTO)
        Mockito.`when`(basePreferencesAdapter.getAuthToken()).thenReturn(authTokenDTO)
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(apiProvider.refreshToken(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenReturn(GlobalScope.async { authTokenDTO })
        refreshTokenViewModel.doAsyncRequest(params = "")
        Assertions.assertThat(refreshTokenViewModel.lastViewState).isInstanceOf(ViewState.Success::class.java)
    }
}