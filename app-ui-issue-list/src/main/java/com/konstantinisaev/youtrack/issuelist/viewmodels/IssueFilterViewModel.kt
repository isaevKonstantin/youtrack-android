package com.konstantinisaev.youtrack.issuelist.viewmodels

import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.issuelist.viewmodels.IssueListType.Companion.findListType
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import javax.inject.Inject

class IssueFilterViewModel @Inject constructor(private val basePreferencesAdapter: BasePreferencesAdapter,
                                               coroutineHolder: CoroutineContextHolder
) : BaseViewModel<String>(coroutineHolder){

    var issueListType: IssueListType? = null
        private set

    override suspend fun execute(params: String?): ViewState {
        return if(issueListType == null){
            issueListType = findListType(basePreferencesAdapter.getIssueListType())
            ViewState.Success(this::class.java,issueListType)
        }else{
            issueListType = switchToNextListType(IssueListType.findListType(issueListType?.type ?: 0))
            basePreferencesAdapter.setIssueListType(issueListType?.type ?: 0)
            ViewState.Success(this::class.java,issueListType)
        }
    }

    private fun switchToNextListType(currentListType: IssueListType) : IssueListType{
        return when(currentListType){
            IssueListType.SINGLE_VIEW -> {
                IssueListType.COMPACT_VIEW
            }
            IssueListType.COMPACT_VIEW -> {
                IssueListType.DETAILED_VIEW
            }
            else -> {
                IssueListType.SINGLE_VIEW
            }
        }
    }

}

enum class IssueListType(val type: Int){

    SINGLE_VIEW(100),
    COMPACT_VIEW(200),
    DETAILED_VIEW(300);

    companion object {

        val map = hashMapOf(SINGLE_VIEW.type to SINGLE_VIEW, COMPACT_VIEW.type to COMPACT_VIEW, DETAILED_VIEW.type to DETAILED_VIEW)

        fun findListType(listType: Int) = map[listType] ?: SINGLE_VIEW
    }

}