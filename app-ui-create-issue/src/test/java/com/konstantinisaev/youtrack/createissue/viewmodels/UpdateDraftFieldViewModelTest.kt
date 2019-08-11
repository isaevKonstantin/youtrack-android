package com.konstantinisaev.youtrack.createissue.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.FieldContainerDTO
import com.konstantinisaev.youtrack.core.api.ProjectCustomFieldDto
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
class UpdateDraftFieldViewModelTest {

    private lateinit var updateDraftFieldViewModel: UpdateDraftFieldViewModel

    @Mock
    private lateinit var apiProvider: ApiProvider
    @Mock
    private lateinit var basePreferencesAdapter: BasePreferencesAdapter
    private val param = UpdateDraftField("", "", "")

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        updateDraftFieldViewModel = UpdateDraftFieldViewModel(
            apiProvider, basePreferencesAdapter,
            testCoroutineContextHolder
        )
    }

    @Test
    fun `given error response should produce error state`() {
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(basePreferencesAdapter.getLastDraftId()).thenReturn("")
        Mockito.`when`(apiProvider.updateDraftField(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyMap<String,String>()
        )).thenThrow(RuntimeException("test"))
        updateDraftFieldViewModel.doAsyncRequest(param)
        Assertions.assertThat(updateDraftFieldViewModel.lastViewState).isInstanceOf(ViewState.Error::class.java)
    }

    @Test
    fun `given success response should produce success state`() {
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(basePreferencesAdapter.getLastDraftId()).thenReturn("")
        Mockito.`when`(apiProvider.updateDraftField(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyMap<String,String>()
        )).
            thenReturn(GlobalScope.async(testCoroutineContextHolder.main()) { FieldContainerDTO(projectCustomField = ProjectCustomFieldDto()) })
        updateDraftFieldViewModel.doAsyncRequest(param)
        Assertions.assertThat(updateDraftFieldViewModel.lastViewState).isInstanceOf(ViewState.Success::class.java)
    }

}