package com.konstantinisaev.youtrack.ui.auth.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.konstantinisaev.youtrack.core.api.*
import com.konstantinisaev.youtrack.ui.auth.R
import com.konstantinisaev.youtrack.ui.auth.testCoroutineContextHolder
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
@Suppress("DeferredResultUnused")
class AuthByLoginPasswordViewModelTest {

    private lateinit var authViewModel: BaseViewModel<AuthByLoginPasswordParam>

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiProvider: ApiProvider
    @Mock
    private lateinit var basePreferencesAdapter: BasePreferencesAdapter

    @Before
    fun setUp() {
        authViewModel = AuthByLoginPasswordViewModel(apiProvider,basePreferencesAdapter, testCoroutineContextHolder)
    }

    @Test
    fun `given empty body should produce validation error state`() {
        authViewModel.doAsyncRequest()
        assertThat(authViewModel.lastViewState).isExactlyInstanceOf(ViewState.ValidationError::class.java)
    }

    @Test
    fun `given empty login should produce validation error state`() {
        authViewModel.doAsyncRequest(AuthByLoginPasswordParam("","password"))
        assertThat(authViewModel.lastViewState).isExactlyInstanceOf(ViewState.ValidationError::class.java)
        assertThat((authViewModel.lastViewState as ViewState.ValidationError).msgId).isEqualTo(R.string.auth_empty_login_error)
    }

    @Test
    fun `given empty password should produce validation error state`() {
        authViewModel.doAsyncRequest(AuthByLoginPasswordParam("1",""))
        assertThat(authViewModel.lastViewState).isExactlyInstanceOf(ViewState.ValidationError::class.java)
        assertThat((authViewModel.lastViewState as ViewState.ValidationError).msgId).isEqualTo(R.string.auth_empty_password_error)
    }

    @Test
    fun `given empty server config should product error state`() {
        authViewModel.doAsyncRequest(AuthByLoginPasswordParam("1","1"))
        assertThat(authViewModel.lastViewState).isExactlyInstanceOf(ViewState.ValidationError::class.java)
        assertThat((authViewModel.lastViewState as ViewState.ValidationError).msgId).isEqualTo(R.string.auth_empty_server_config_error)
    }

    @Test
    fun `given error credentials should produce error state`() {
        `when`(basePreferencesAdapter.getServerConfig()).thenReturn(
            ServerConfigDTO(
                "version",
                MobileConfigDTO("secret","id","type"),
                RingConfigDTO("id","url","type"),
                false,
                "type"
            ))
        `when`(basePreferencesAdapter.getUrl()).thenReturn("url")
        `when`(apiProvider.login(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString())).thenThrow(RuntimeException("test"))
        authViewModel.doAsyncRequest(AuthByLoginPasswordParam("1","1"))
        assertThat(authViewModel.lastViewState).isInstanceOf(ViewState.Error::class.java)
    }

    @Test
    fun `given valid credentials should produce success state`() {
        `when`(basePreferencesAdapter.getServerConfig()).thenReturn(
            ServerConfigDTO(
                "version",
                MobileConfigDTO("secret","id","type"),
                RingConfigDTO("id","url","type"),
                false,
                "type"
            ))
        `when`(basePreferencesAdapter.getUrl()).thenReturn("url")
        `when`(apiProvider.login(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString())).thenReturn(GlobalScope.async {  AuthTokenDTO("","",1,"","") })
        authViewModel.doAsyncRequest(AuthByLoginPasswordParam("1","1"))
        Mockito.verify(basePreferencesAdapter, times(1)).setAuthToken(ArgumentMatchers.any(AuthTokenDTO::class.java))
        assertThat(authViewModel.lastViewState).isInstanceOf(ViewState.Success::class.java)
    }
}