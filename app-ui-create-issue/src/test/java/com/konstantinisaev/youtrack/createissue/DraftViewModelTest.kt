package com.konstantinisaev.youtrack.createissue

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.core.api.IssueDTO
import com.konstantinisaev.youtrack.core.api.ProjectDTO
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import kotlinx.coroutines.*
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

@RunWith(MockitoJUnitRunner.Silent::class)
class DraftViewModelTest {


    private lateinit var draftViewModel: DraftViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiProvider: ApiProvider
    @Mock
    private lateinit var basePreferencesAdapter: BasePreferencesAdapter
    private val draftOrProjectId = "draftOrProjectId"
    private val url = "url"

    @Before
    fun setUp() {
        draftViewModel = DraftViewModel(apiProvider,basePreferencesAdapter, testCoroutineContextHolder)
    }

    @Test
    fun `given empty draft id and project id should produce error state`() {
        Mockito.`when`(basePreferencesAdapter.getLastDraftId()).thenReturn("")
        Mockito.`when`(basePreferencesAdapter.getCurrentProjectId()).thenReturn("")
        draftViewModel.doAsyncRequest()
        Assertions.assertThat(draftViewModel.lastViewState).isInstanceOf(ViewState.ValidationError::class.java)
    }

    @Test
    fun `given error when init draft should produce error state`() {
        Mockito.`when`(basePreferencesAdapter.getLastDraftId()).thenReturn("")
        Mockito.`when`(basePreferencesAdapter.getCurrentProjectId()).thenReturn(draftOrProjectId)
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn(url)
        Mockito.`when`(apiProvider.initDraft(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenThrow(RuntimeException("test"))
        draftViewModel.doAsyncRequest()
        Assertions.assertThat(draftViewModel.lastViewState).isInstanceOf(ViewState.Error::class.java)
    }

    @Test
    fun `given error when try to get draft should produce error state`() {
        Mockito.`when`(basePreferencesAdapter.getCurrentProjectId()).thenReturn("")
        Mockito.`when`(basePreferencesAdapter.getLastDraftId()).thenReturn(draftOrProjectId)
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn(url)
        Mockito.`when`(apiProvider.getIssueByDraftId(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenThrow(RuntimeException("test"))
        draftViewModel.doAsyncRequest()
        Assertions.assertThat(draftViewModel.lastViewState).isInstanceOf(ViewState.Error::class.java)
    }

    @Test
    fun `given error when try to get draft should init draft`() {
        Mockito.`when`(basePreferencesAdapter.getCurrentProjectId()).thenReturn("")
        Mockito.`when`(basePreferencesAdapter.getLastDraftId()).thenReturn(draftOrProjectId)
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn(url)
        Mockito.`when`(apiProvider.getIssueByDraftId(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenThrow(RuntimeException("test"))
        Mockito.`when`(apiProvider.initDraft(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenThrow(RuntimeException("test from init draft"))
        draftViewModel.doAsyncRequest()
        Mockito.verify(apiProvider,times(1)).initDraft(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())
        Assertions.assertThat(draftViewModel.lastViewState).isInstanceOf(ViewState.Error::class.java)
    }

    @Test
    fun `given success when init draft should produce success state`() {
        Mockito.`when`(basePreferencesAdapter.getLastDraftId()).thenReturn("")
        Mockito.`when`(basePreferencesAdapter.getCurrentProjectId()).thenReturn(draftOrProjectId)
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn(url)
        Mockito.`when`(apiProvider.initDraft(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenReturn(GlobalScope.async(
            testCoroutineContextHolder.main()) { IssueDTO("","","","","","","", listOf(),null,0,null, listOf(),
            ProjectDTO("","","","",false,"")
        ) })
        draftViewModel.doAsyncRequest()
        Assertions.assertThat(draftViewModel.lastViewState).isInstanceOf(ViewState.Success::class.java)
    }

    @Test
    fun `given success when getting draft should produce success state`() {
        Mockito.`when`(basePreferencesAdapter.getLastDraftId()).thenReturn(draftOrProjectId)
        Mockito.`when`(basePreferencesAdapter.getCurrentProjectId()).thenReturn("")
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn(url)
        Mockito.`when`(apiProvider.getIssueByDraftId(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenReturn(GlobalScope.async(
            testCoroutineContextHolder.main()) { IssueDTO("","","","","","","", listOf(),null,0,null, listOf(),
            ProjectDTO("","","","",false,"")
        ) })
        draftViewModel.doAsyncRequest()
        Assertions.assertThat(draftViewModel.lastViewState).isInstanceOf(ViewState.Success::class.java)
    }

    @Test
    fun `given success when init draft,after error in getting draft should produce success state`() {
        Mockito.`when`(basePreferencesAdapter.getLastDraftId()).thenReturn("")
        Mockito.`when`(basePreferencesAdapter.getCurrentProjectId()).thenReturn(draftOrProjectId)
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn(url)
        Mockito.`when`(apiProvider.getIssueByDraftId(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenThrow(RuntimeException("test"))
        Mockito.`when`(apiProvider.initDraft(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenReturn(GlobalScope.async(
            testCoroutineContextHolder.main()) { IssueDTO("","","","","","","", listOf(),null,0,null, listOf(),
            ProjectDTO("","","","",false,"")
        ) })
        draftViewModel.doAsyncRequest()
        Assertions.assertThat(draftViewModel.lastViewState).isInstanceOf(ViewState.Success::class.java)
    }
}

@ExperimentalCoroutinesApi
val testCoroutineContextHolder = object : CoroutineContextHolder() {
    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun main(): CoroutineDispatcher = Dispatchers.Unconfined
}
