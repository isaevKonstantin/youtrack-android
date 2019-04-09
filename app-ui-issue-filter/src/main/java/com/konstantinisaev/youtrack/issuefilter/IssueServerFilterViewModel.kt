package com.konstantinisaev.youtrack.issuefilter

import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.core.api.DEFAULT_ISSUE_LIST_SIZE
import com.konstantinisaev.youtrack.core.rv.IssueFilterSuggestionChildRvItem
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.models.IssueFilterSuggest
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import java.util.*
import javax.inject.Inject

class IssueServerFilterViewModel @Inject constructor(private val apiProvider: ApiProvider,
                                                     private val basePreferencesAdapter: BasePreferencesAdapter,
                                                     private val issueFilterSuggestionHolder: IssueFilterSuggestionHolder,
                                                     coroutineContextHolder: CoroutineContextHolder) : BaseViewModel<IssueServerFilterViewModel.IssueServerFilterParam>(coroutineContextHolder) {

    override suspend fun execute(params: IssueServerFilterParam?): ViewState {
        params ?: return ViewState.Success(this::class.java,issueFilterSuggestionHolder.initFilter())

        val cache = issueFilterSuggestionHolder.getCache(params.parentUuid).map { it }.toList()
        if(cache.isNotEmpty()){
            return ViewState.Success(this::class.java,cache)
        }
        val resp = apiProvider.getIssuesFilterIntellisense(basePreferencesAdapter.getUrl(),params.query,params.caret).await()
        val data = resp.suggestDTO.items.
            filter { !it.option.isNullOrEmpty() && !it.option.orEmpty().matches("[*-\\-]".toRegex()) }.
            map { IssueFilterSuggest(it.styleClass,it.prefix,it.option,it.suffix,it.description,it.htmlDescription,it.caret,it.completion,it.match,
                parentUuid = params.parentUuid,checked = false) }
        issueFilterSuggestionHolder.addChildes(data)
        return ViewState.Success(this::class.java,data)
    }

    data class IssueServerFilterParam(val query: String? = null,val caret: Int? = null,val optionsLimit: Int = DEFAULT_ISSUE_LIST_SIZE,val parentUuid: String = "")

    fun formatCheckedValuesToStr(childList: List<IssueFilterSuggestionChildRvItem>) : String {
        val sb = StringBuilder()
        val parentUuidSet = hashSetOf<String>()
        childList.forEach {
            if(parentUuidSet.contains(it.parentUuid)){
                sb.append(",")
                sb.append(it.issueFilterSuggestionRvData.name)
            }else{
                sb.append(" ")
                sb.append(issueFilterSuggestionHolder.formatCheckedValueToStr(it.parentUuid,it.issueFilterSuggestionRvData.uuid))
                parentUuidSet.add(it.parentUuid)
            }
        }
        return sb.toString()
    }

}

@Suppress("JoinDeclarationAndAssignment")
class IssueFilterSuggestionHolder(parentsStr: Array<String>) {

    private val parentsMap: HashMap<String,Pair<IssueFilterSuggest,List<IssueFilterSuggest>>>
    private val parents: List<IssueFilterSuggest>

    init {
        parents = parentsStr.map { IssueFilterSuggest(option = it,uuid = UUID.randomUUID().toString()) }
        parentsMap = hashMapOf()
        parents.forEach {
            parentsMap[it.uuid] = Pair(it, Collections.emptyList())
        }
    }

    fun initFilter() = parents

    fun addChildes(childList: List<IssueFilterSuggest>) {
        val parentUuid = childList.firstOrNull()?.parentUuid.orEmpty()
        parentUuid.takeIf{ it.isNotEmpty() }?.let {
            val pair = parentsMap[it]
            pair ?: return@let
            val list = pair.second.toMutableList().apply {
                clear()
                addAll(childList)
            }
            parentsMap[it] = pair.copy(second = list)
        }
    }

    fun getCache(uuid: String): List<IssueFilterSuggest> {
        return parentsMap[uuid]?.second.orEmpty()
    }

    fun getParentName(uuid: String) : String{
        return parentsMap[uuid]?.first?.option.orEmpty()
    }

    fun formatCheckedValueToStr(parentUuid: String,uuid: String) : String{
        if(!parentsMap.containsKey(parentUuid)){
            return ""
        }
        val pair = parentsMap.getValue(parentUuid)
        val child = pair.second.find { it.uuid  == uuid }
        var req = ""
        child?.let {
            val fullChildReq = "${it.prefix.orEmpty()}${it.option}${it.suffix.orEmpty()}"
            req = "${pair.first.option}: $fullChildReq"
        }
        return req
    }
}

