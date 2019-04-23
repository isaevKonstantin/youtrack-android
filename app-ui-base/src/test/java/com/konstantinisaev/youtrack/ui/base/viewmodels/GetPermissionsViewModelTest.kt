package com.konstantinisaev.youtrack.ui.base.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.MobileConfigDTO
import com.konstantinisaev.youtrack.core.api.RingConfigDTO
import com.konstantinisaev.youtrack.core.api.ServerConfigDTO
import com.konstantinisaev.youtrack.core.api.models.CachedPermissionDTO
import com.konstantinisaev.youtrack.core.api.models.PermissionHolder
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
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
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner.Silent::class)
@Suppress("DeferredResultUnused")
class GetPermissionsViewModelTest {

    private lateinit var getPermissionsViewModel: GetPermissionsViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiProvider: ApiProvider
    @Mock
    private lateinit var basePreferencesAdapter: BasePreferencesAdapter
    @Mock
    private lateinit var permissionHolder: PermissionHolder

    @Before
    fun setUp() {
        getPermissionsViewModel = GetPermissionsViewModel(apiProvider,basePreferencesAdapter,permissionHolder,testCoroutineContextHolder)
    }

    @Test
    fun `given null config should produce error state`() {
        getPermissionsViewModel.doAsyncRequest()
        Assertions.assertThat(getPermissionsViewModel.lastViewState).isInstanceOf(ViewState.Error::class.java)
    }

    @Test
    fun `given error response should produce error state`() {
        Mockito.`when`(basePreferencesAdapter.getServerConfig()).thenReturn(
            ServerConfigDTO(
                "",
                MobileConfigDTO("","",""),
                RingConfigDTO("","",""),
                true,
                ""
            ))
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(apiProvider.getPermissions(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenThrow(RuntimeException("test"))
        getPermissionsViewModel.doAsyncRequest()
        Assertions.assertThat(getPermissionsViewModel.lastViewState).isInstanceOf(ViewState.Error::class.java)
    }

    @Test
    fun `given valid response should produce success state`() {
        Mockito.`when`(basePreferencesAdapter.getServerConfig()).thenReturn(
            ServerConfigDTO(
                "",
                MobileConfigDTO("","",""),
                RingConfigDTO("","",""),
                true,
                ""
            ))
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(apiProvider.getPermissions(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenReturn(
            GlobalScope.async(testCoroutineContextHolder.main()) { listOf<CachedPermissionDTO>() })
        getPermissionsViewModel.doAsyncRequest()
        Mockito.verify(permissionHolder,times(1)).init(Mockito.anyList())
        Assertions.assertThat(getPermissionsViewModel.lastViewState).isInstanceOf(ViewState.Success::class.java)
    }
}