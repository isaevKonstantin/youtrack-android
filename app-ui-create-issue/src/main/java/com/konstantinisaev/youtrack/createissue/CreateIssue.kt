package com.konstantinisaev.youtrack.createissue

import androidx.lifecycle.ViewModel
import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.di.FeatureViewModelFactoryModule
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewModelFactory
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewModelKey
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [CreateIssueViewModels::class])
internal interface CreateIssueComponent{

    fun injectFragment(createIssueDialog: CreateIssueDialog)

    @Component.Builder
    interface Builder{

        fun build(): CreateIssueComponent

        @BindsInstance
        fun apiProvider(apiProvider: ApiProvider) : Builder

        @BindsInstance
        fun preferenceAdapter(basePreferencesAdapter: BasePreferencesAdapter) : Builder

        @BindsInstance
        fun coroutineContextHolder(coroutineContextHolder: CoroutineContextHolder) : Builder

        @BindsInstance
        fun viewModelFactory(@Named("baseFactory") viewModelFactory: ViewModelFactory) : Builder
    }

}

@Suppress("unused")
@Module(includes = [FeatureViewModelFactoryModule::class])
abstract class CreateIssueViewModels{

    @Binds
    @IntoMap
    @ViewModelKey(DraftViewModel::class)
    internal abstract fun bindDraftViewModel(draftViewModel: DraftViewModel): ViewModel
}

class CreateIssueDiProvider private constructor(){

    fun injectFragment(createIssueDialog: CreateIssueDialog){
        component.injectFragment(createIssueDialog)
    }

    companion object {

        private lateinit var component: CreateIssueComponent

        private val diProvider by lazy {
            CreateIssueDiProvider()
        }

        fun init(
            apiProvider: ApiProvider,
            basePreferencesAdapter: BasePreferencesAdapter,
            coroutineContextHolder: CoroutineContextHolder,
            viewModelFactory: ViewModelFactory
        ){
            if(!this::component.isInitialized){
                component = DaggerCreateIssueComponent.builder().apiProvider(apiProvider).viewModelFactory(viewModelFactory).
                    preferenceAdapter(basePreferencesAdapter).coroutineContextHolder(coroutineContextHolder).build()
            }
        }

        fun getInstance() : CreateIssueDiProvider {
            return diProvider
        }
    }


}
