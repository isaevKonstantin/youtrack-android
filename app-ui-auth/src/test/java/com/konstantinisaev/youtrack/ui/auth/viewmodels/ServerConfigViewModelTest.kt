package com.konstantinisaev.youtrack.ui.auth.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.konstantinisaev.youtrack.core.api.*
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.utils.Base64Converter
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import kotlinx.coroutines.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
@Suppress("DeferredResultUnused")
class ServerConfigViewModelTest {

    private lateinit var urlViewModel: BaseViewModel<String>

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiProvider: ApiProvider
    @Mock
    private lateinit var basePreferencesAdapter: BasePreferencesAdapter
    @Mock
    private lateinit var base64Converter: Base64Converter

    private val testCoroutineContextHolder = object : CoroutineContextHolder() {
        override fun io(): CoroutineDispatcher = Dispatchers.Unconfined
        override fun main(): CoroutineDispatcher = Dispatchers.Unconfined
    }

    @Before
    fun setUp() {
        urlViewModel = ServerConfigViewModel(apiProvider,basePreferencesAdapter, base64Converter,testCoroutineContextHolder)
        Dispatchers
    }

    @Test
    fun `given foreign url should produce error state`() {
        `when`(basePreferencesAdapter.getUrl()).thenReturn("http://1.com")
        urlViewModel.doAsyncRequest("")
        verify(apiProvider, times(2)).getServerConfig(ArgumentMatchers.anyString())
        assertThat(urlViewModel.lastViewState is ViewState.Error).isTrue()
    }

    @Test
    fun `given empty url should produce empty state`() {
        `when`(basePreferencesAdapter.getUrl()).thenReturn("")
        urlViewModel.doAsyncRequest(params = "")
        assertThat(urlViewModel.lastViewState is ViewState.Empty).isTrue()
    }

    @Test
    fun `given empty config should produce error state`() {
        `when`(basePreferencesAdapter.getUrl()).thenReturn("http://1.com")
        `when`(apiProvider.getServerConfig(ArgumentMatchers.anyString())).thenReturn(GlobalScope.async { ServerConfigDTO("",
            MobileConfigDTO("","",""),RingConfigDTO("","",""),false,"") })
        urlViewModel.doAsyncRequest("")
        verify(apiProvider, times(1)).getServerConfig(ArgumentMatchers.anyString())
        assertThat(urlViewModel.lastViewState is ViewState.Error).isTrue()
    }

    @Test
    fun `given valid config should save it and produce success state`() {
        `when`(basePreferencesAdapter.getUrl()).thenReturn("http://1.com")
        `when`(base64Converter.convertToBase64(ArgumentMatchers.anyString())).thenReturn("")
        `when`(apiProvider.getServerConfig(ArgumentMatchers.anyString())).thenReturn(GlobalScope.async { ServerConfigDTO("",
            MobileConfigDTO("1","1",""),RingConfigDTO("","",""),false,"") })
        urlViewModel.doAsyncRequest("")
        verify(apiProvider, times(1)).getServerConfig(ArgumentMatchers.anyString())
        verify(apiProvider, times(1)).enableAppCredentialsInHeader(ArgumentMatchers.anyString())
        verify(basePreferencesAdapter, times(1)).setUrl(ArgumentMatchers.anyString())
        verify(basePreferencesAdapter, times(1)).setServerConfig(ArgumentMatchers.any())
        assertThat(urlViewModel.lastViewState is ViewState.Success<*>).isTrue()
    }
}