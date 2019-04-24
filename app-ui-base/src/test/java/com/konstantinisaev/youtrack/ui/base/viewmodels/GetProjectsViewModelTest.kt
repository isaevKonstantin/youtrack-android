package com.konstantinisaev.youtrack.ui.base.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.konstantinisaev.youtrack.core.api.*
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
class GetProjectsViewModelTest {

    private lateinit var getProjectsViewModel: GetProjectsViewModel

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
        getProjectsViewModel = GetProjectsViewModel(apiProvider,basePreferencesAdapter, permissionHolder,testCoroutineContextHolder)
    }

    @Test
    fun `given error project list response should produce error state`() {
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(permissionHolder.isEmpty()).thenReturn(false)
        Mockito.`when`(apiProvider.getProjects(
            ArgumentMatchers.anyString())).thenThrow(RuntimeException("test"))
        getProjectsViewModel.doAsyncRequest("")
        Assertions.assertThat(getProjectsViewModel.lastViewState).isExactlyInstanceOf(ViewState.Error::class.java)
    }

    @Test
    fun `given success issue count response should produce success state`() {
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(permissionHolder.isEmpty()).thenReturn(false)
        Mockito.`when`(apiProvider.getProjects(ArgumentMatchers.anyString())).thenReturn(
            GlobalScope.async(testCoroutineContextHolder.main()) { listOf(ProjectDTO("","","","",true,"ringId"))  })
        getProjectsViewModel.doAsyncRequest("")
        Mockito.verify(basePreferencesAdapter,times(1)).setCurrentProjectId(ArgumentMatchers.anyString())
        Assertions.assertThat(getProjectsViewModel.lastViewState).isExactlyInstanceOf(ViewState.Success::class.java)
    }

    @Test
    fun `given error permissions response should produce error state`() {
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(permissionHolder.isEmpty()).thenReturn(true)
        Mockito.`when`(apiProvider.getPermissions(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenThrow(RuntimeException("test"))
        getProjectsViewModel.doAsyncRequest("")
        Assertions.assertThat(getProjectsViewModel.lastViewState).isExactlyInstanceOf(ViewState.Error::class.java)
    }

    @Test
    fun `given success responses should produce success state`() {
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(permissionHolder.isEmpty()).thenReturn(true)
        Mockito.`when`(basePreferencesAdapter.getServerConfig()).thenReturn(ServerConfigDTO("",MobileConfigDTO("","",""),
            RingConfigDTO("","",""),true,""
        ))
        Mockito.`when`(apiProvider.getProjects(ArgumentMatchers.anyString())).thenReturn(
            GlobalScope.async(testCoroutineContextHolder.main()) { listOf(ProjectDTO("","","","",true,"ringId"))  })
        Mockito.`when`(apiProvider.getPermissions(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenReturn(
            GlobalScope.async(testCoroutineContextHolder.main()) { listOf<CachedPermissionDTO>()  })
        getProjectsViewModel.doAsyncRequest("")
        Mockito.verify(permissionHolder,Mockito.times(1)).init(ArgumentMatchers.anyList())
        Assertions.assertThat(getProjectsViewModel.lastViewState).isExactlyInstanceOf(ViewState.Success::class.java)
    }
}