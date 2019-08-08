package com.konstantinisaev.youtrack.createissue.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.UserDTO
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
class GetFieldUserSettingsViewModelTest {

    private lateinit var getFieldUserSettingViewModel: GetFieldUserSettingsViewModel

    @Mock
    private lateinit var apiProvider: ApiProvider
    @Mock
    private lateinit var basePreferencesAdapter: BasePreferencesAdapter

    private val testParam = CreateIssueFieldParam("", "")

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        getFieldUserSettingViewModel = GetFieldUserSettingsViewModel(
            apiProvider, basePreferencesAdapter,
            testCoroutineContextHolder
        )
    }

    @Test
    fun `given error response should produce error state`() {
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(apiProvider.getCustomFieldUserSettings(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString())).thenThrow(RuntimeException("test"))
        getFieldUserSettingViewModel.doAsyncRequest(testParam)
        Assertions.assertThat(getFieldUserSettingViewModel.lastViewState).isInstanceOf(ViewState.Error::class.java)
    }

    @Test
    fun `given success response should produce success state`() {
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(apiProvider.getCustomFieldUserSettings(ArgumentMatchers.anyString(),ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).
            thenReturn(GlobalScope.async(testCoroutineContextHolder.main()) { listOf<UserDTO>() })
        getFieldUserSettingViewModel.doAsyncRequest(testParam)
        Assertions.assertThat(getFieldUserSettingViewModel.lastViewState).isInstanceOf(ViewState.Success::class.java)
    }


}