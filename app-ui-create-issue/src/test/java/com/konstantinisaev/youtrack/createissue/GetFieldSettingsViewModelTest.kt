package com.konstantinisaev.youtrack.createissue

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CustomFieldAdminDTO
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
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

@Suppress("EXPERIMENTAL_API_USAGE")
@RunWith(MockitoJUnitRunner.Silent::class)
class GetFieldSettingsViewModelTest {

    private lateinit var getFieldSettingsViewModel: GetFieldSettingsViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiProvider: ApiProvider
    @Mock
    private lateinit var basePreferencesAdapter: BasePreferencesAdapter
    private val param = GetFieldSettingsViewModel.Param("", "")

    @Before
    fun setUp() {
        getFieldSettingsViewModel = GetFieldSettingsViewModel(apiProvider,basePreferencesAdapter,
            testCoroutineContextHolder)
    }

    @Test
    fun `given error response should produce error state`() {
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(apiProvider.getCustomFieldSettings(ArgumentMatchers.anyString(),ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenThrow(RuntimeException("test"))
        getFieldSettingsViewModel.doAsyncRequest(param)
        Assertions.assertThat(getFieldSettingsViewModel.lastViewState).isInstanceOf(ViewState.Error::class.java)
    }

    @Test
    fun `given success response should produce success state`() {
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(apiProvider.getCustomFieldSettings(ArgumentMatchers.anyString(),ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).
            thenReturn(GlobalScope.async(testCoroutineContextHolder.main()) { listOf<CustomFieldAdminDTO>() })
        getFieldSettingsViewModel.doAsyncRequest(param)
        Assertions.assertThat(getFieldSettingsViewModel.lastViewState).isInstanceOf(ViewState.Success::class.java)
    }
}