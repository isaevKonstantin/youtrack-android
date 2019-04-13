package com.konstantinisaev.youtrack.issuefilter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.core.api.FilterAutoCompleteDTO
import com.konstantinisaev.youtrack.core.api.SuggestItemsDTO
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.models.IssueFilterSuggest
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
import org.mockito.Mockito.never
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner.Silent::class)
@Suppress("DeferredResultUnused")
class IssueServerFilterViewModelTest{

    private lateinit var issueServerFilterViewModel: IssueServerFilterViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiProvider: ApiProvider
    @Mock
    private lateinit var basePreferencesAdapter: BasePreferencesAdapter
    @Mock
    private lateinit var issueFilterSuggestionHolder: IssueFilterSuggestionHolder


    @Before
    fun setUp() {
        issueServerFilterViewModel = IssueServerFilterViewModel(apiProvider,basePreferencesAdapter,issueFilterSuggestionHolder,object : CoroutineContextHolder() {
            override fun io(): CoroutineDispatcher = Dispatchers.Unconfined
            override fun main(): CoroutineDispatcher = Dispatchers.Unconfined
        })
    }

    @Test
    fun `given null params should return initialization list`() {
        Mockito.`when`(issueFilterSuggestionHolder.initFilter()).thenReturn(listOf())
        issueServerFilterViewModel.doAsyncRequest()
        Mockito.verify(basePreferencesAdapter,never()).getUrl()
        Assertions.assertThat(issueServerFilterViewModel.lastViewState).isExactlyInstanceOf(ViewState.Success::class.java)
    }

    @Test
    fun `given valid params should return list from cache`() {
        Mockito.`when`(issueFilterSuggestionHolder.getCache(ArgumentMatchers.anyString())).thenReturn(listOf(IssueFilterSuggest()))
        issueServerFilterViewModel.doAsyncRequest(IssueServerFilterViewModel.IssueServerFilterParam(""))
        Mockito.verify(basePreferencesAdapter,never()).getUrl()
        Assertions.assertThat(issueServerFilterViewModel.lastViewState).isExactlyInstanceOf(ViewState.Success::class.java)
    }

    @Test
    fun `given error params should produce error state`() {
        Mockito.`when`(issueFilterSuggestionHolder.getCache(ArgumentMatchers.anyString())).thenReturn(listOf())
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(apiProvider.getIssuesFilterIntellisense(ArgumentMatchers.anyString(),ArgumentMatchers.anyString(),ArgumentMatchers.anyInt(),ArgumentMatchers.anyInt())).thenThrow(RuntimeException("test"))
        issueServerFilterViewModel.doAsyncRequest(IssueServerFilterViewModel.IssueServerFilterParam("1",1))
        Assertions.assertThat(issueServerFilterViewModel.lastViewState).isExactlyInstanceOf(ViewState.Error::class.java)
    }

    @Test
    fun `given valid params should return server list`() {
        Mockito.`when`(issueFilterSuggestionHolder.getCache(ArgumentMatchers.anyString())).thenReturn(listOf(IssueFilterSuggest()))
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(apiProvider.getIssuesFilterIntellisense(ArgumentMatchers.anyString(),ArgumentMatchers.anyString(),ArgumentMatchers.anyInt(),ArgumentMatchers.anyInt())).
            thenReturn(GlobalScope.async { FilterAutoCompleteDTO(SuggestItemsDTO(listOf()), listOf(), listOf()) })
        issueServerFilterViewModel.doAsyncRequest(IssueServerFilterViewModel.IssueServerFilterParam(""))
        Assertions.assertThat(issueServerFilterViewModel.lastViewState).isExactlyInstanceOf(ViewState.Success::class.java)
    }
}