package com.konstantinisaev.youtrack.issuelist.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.core.api.CurrentUserDTO
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
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
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner.Silent::class)
@Suppress("DeferredResultUnused")
class ProfileViewModelTest {

    private lateinit var profileViewModel: BaseViewModel<String>

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiProvider: ApiProvider
    @Mock
    private lateinit var basePreferencesAdapter: BasePreferencesAdapter

    @Before
    fun setUp() {
        profileViewModel = ProfileViewModel(
            apiProvider,
            basePreferencesAdapter,
            testCoroutineContextHolder
        )
    }

    @Test
    fun `given error profile response should produce error state`() {
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(apiProvider.getProfile(ArgumentMatchers.anyString())).thenThrow(RuntimeException("test"))
        profileViewModel.doAsyncRequest()
        Assertions.assertThat(profileViewModel.lastViewState).isExactlyInstanceOf(ViewState.Error::class.java)
    }

    @Test
    fun `given success profile response should produce success state`() {
        Mockito.`when`(basePreferencesAdapter.getUrl()).thenReturn("")
        Mockito.`when`(apiProvider.getProfile(ArgumentMatchers.anyString())).thenReturn(GlobalScope.async(
            testCoroutineContextHolder.io()) {  CurrentUserDTO("","","","",false,false,"","") })
        profileViewModel.doAsyncRequest()
        Assertions.assertThat(profileViewModel.lastViewState).isExactlyInstanceOf(ViewState.Success::class.java)
    }
}

@ExperimentalCoroutinesApi
val testCoroutineContextHolder = object : CoroutineContextHolder() {
    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun main(): CoroutineDispatcher = Dispatchers.Unconfined
}
