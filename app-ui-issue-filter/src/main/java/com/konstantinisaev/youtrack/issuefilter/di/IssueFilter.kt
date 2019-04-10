package com.konstantinisaev.youtrack.issuefilter.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.issuefilter.IssueFilterFragment
import com.konstantinisaev.youtrack.issuefilter.IssueFilterSuggestionHolder
import com.konstantinisaev.youtrack.issuefilter.IssueServerFilterViewModel
import com.konstantinisaev.youtrack.issuefilter.R
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewModelFactory
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewModelKey
import dagger.*
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Singleton
@Component(modules = [IssueFilterModule::class])
internal interface IssueFilterComponent {

    fun injectFragment(issueFilterFragment: IssueFilterFragment)

    @Component.Builder
    interface Builder {

        fun build(): IssueFilterComponent

        @BindsInstance
        fun apiProvider(apiProvider: ApiProvider): Builder

        @BindsInstance
        fun preferenceAdapter(basePreferencesAdapter: BasePreferencesAdapter): Builder

        @BindsInstance
        fun coroutineContextHolder(coroutineContextHolder: CoroutineContextHolder): Builder

        @BindsInstance
        fun context(context: Context): Builder

        @BindsInstance
        fun viewModelFactory(viewModelFactory: ViewModelProvider.Factory) : Builder
    }
}

@Suppress("unused")
@Module
abstract class IssueFilterViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(IssueServerFilterViewModel::class)
    internal abstract fun bindIssueServerFilterViewModel(issueServerFilterViewModel: IssueServerFilterViewModel): ViewModel

}

@Suppress("unused")
@Module
class IssueFilterModule{

    @Provides
    @Singleton
    fun provideIssueFilterSuggestionHolder(context: Context) : IssueFilterSuggestionHolder = IssueFilterSuggestionHolder(context.resources.getStringArray(R.array.issue_filter_arr))
}

class IssueFilterDiProvider private constructor(){

    fun injectFragment(issueFilterFragment: IssueFilterFragment){
        component.injectFragment(issueFilterFragment)
    }

    companion object {

        private lateinit var component: IssueFilterComponent

        private val diProvider by lazy {
            IssueFilterDiProvider()
        }

        fun init(
            apiProvider: ApiProvider,
            basePreferencesAdapter: BasePreferencesAdapter,
            coroutineContextHolder: CoroutineContextHolder,
            context: Context,
            viewModelFactory: ViewModelFactory
        ){
            if(!this::component.isInitialized){
                component = DaggerIssueFilterComponent.builder().apiProvider(apiProvider).preferenceAdapter(basePreferencesAdapter).viewModelFactory(viewModelFactory).
                    coroutineContextHolder(coroutineContextHolder).context(context).build()
            }
        }

        fun getInstance() : IssueFilterDiProvider {
            return diProvider
        }
    }
}