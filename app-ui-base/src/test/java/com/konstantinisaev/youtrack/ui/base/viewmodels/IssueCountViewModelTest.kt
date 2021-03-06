package com.konstantinisaev.youtrack.ui.base.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.core.api.IssueCountDTO
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
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

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner.Silent::class)
@Suppress("DeferredResultUnused")
class IssueCountViewModelTest {

    private lateinit var issueCountViewModel: BaseViewModel<String>

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiProvider: ApiProvider
    @Mock
    private lateinit var basePreferencesAdapter: BasePreferencesAdapter

    @Before
    fun setUp() {
        issueCountViewModel = IssueCountViewModel(
            apiProvider,
            basePreferencesAdapter,
            testCoroutineContextHolder
        )
    }

    @Test
    fun `given error issue list response should produce error state`() {
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(apiProvider.getAllIssuesCount(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString())).thenThrow(RuntimeException("test"))
        issueCountViewModel.doAsyncRequest("")
        Assertions.assertThat(issueCountViewModel.lastViewState).isExactlyInstanceOf(ViewState.Error::class.java)
    }

    @Test
    fun `given success issue count response should produce success state`() {
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(apiProvider.getAllIssuesCount(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenReturn(
            GlobalScope.async {  IssueCountDTO(-1) })
        issueCountViewModel.doAsyncRequest("")
        Assertions.assertThat(issueCountViewModel.lastViewState).isExactlyInstanceOf(ViewState.Success::class.java)
        Mockito.verify(apiProvider,times(3)).getAllIssuesCount(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())
    }

}

@ExperimentalCoroutinesApi
val testCoroutineContextHolder = object : CoroutineContextHolder() {
    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun main(): CoroutineDispatcher = Dispatchers.Unconfined
}
