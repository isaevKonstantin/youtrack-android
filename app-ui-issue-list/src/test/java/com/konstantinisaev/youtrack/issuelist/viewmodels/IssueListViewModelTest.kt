package com.konstantinisaev.youtrack.issuelist.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.IssueDTO
import com.konstantinisaev.youtrack.issuelist.testCoroutineContextHolder
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
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
@RunWith(MockitoJUnitRunner.Silent::class)
@Suppress("DeferredResultUnused")
class IssueListViewModelTest {

    private lateinit var issueListViewModel: BaseViewModel<IssueListParam>

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiProvider: ApiProvider
    @Mock
    private lateinit var basePreferencesAdapter: BasePreferencesAdapter

    @Before
    fun setUp() {
        issueListViewModel = IssueListViewModel(apiProvider,basePreferencesAdapter, testCoroutineContextHolder)
    }

    @Test
    fun `given error issue list response should produce error state`() {
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(apiProvider.getAllIssues(ArgumentMatchers.anyString(),ArgumentMatchers.anyString(),ArgumentMatchers.anyInt(),ArgumentMatchers.anyInt())).thenThrow(RuntimeException("test"))
        issueListViewModel.doAsyncRequest(IssueListParam("",0,0))
        Assertions.assertThat(issueListViewModel.lastViewState).isExactlyInstanceOf(ViewState.Error::class.java)

    }

    @Test
    fun `given success profile response should produce success state`() {
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(apiProvider.getAllIssues(ArgumentMatchers.anyString(),ArgumentMatchers.anyString(),ArgumentMatchers.anyInt(),ArgumentMatchers.anyInt())).thenReturn(GlobalScope.async {  listOf<IssueDTO>() })
        issueListViewModel.doAsyncRequest(IssueListParam("",0,0))
        Assertions.assertThat(issueListViewModel.lastViewState).isExactlyInstanceOf(ViewState.Success::class.java)
    }
}