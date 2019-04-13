package com.konstantinisaev.youtrack.issuelist

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.konstantinisaev.youtrack.core.api.DEFAULT_ISSUE_LIST_SIZE
import com.konstantinisaev.youtrack.core.api.IssueCountDTO
import com.konstantinisaev.youtrack.core.rv.*
import com.konstantinisaev.youtrack.issuelist.di.IssueListDiProvider
import com.konstantinisaev.youtrack.issuelist.viewmodels.*
import com.konstantinisaev.youtrack.ui.base.models.Issue
import com.konstantinisaev.youtrack.ui.base.screens.BaseFragment
import com.konstantinisaev.youtrack.ui.base.utils.DeviceUtils
import com.konstantinisaev.youtrack.ui.base.utils.IssueListRouter
import com.konstantinisaev.youtrack.ui.base.utils.toFormattedString
import com.konstantinisaev.youtrack.ui.base.utils.toHourAndMinutesString
import com.konstantinisaev.youtrack.ui.base.viewmodels.FilterUpdatedViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.IssueCountViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.UpdateIssueListViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import kotlinx.android.synthetic.main.fragment_issue_list.*
import java.util.*
import javax.inject.Inject

@Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
class IssueListFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_issue_list

    private lateinit var issueListRvAdapter: BaseRvAdapter
    private lateinit var issueListViewModel: IssueListViewModel
    private lateinit var issueListTypeViewModel: IssueListTypeViewModel
    private lateinit var issueCountViewModel: IssueCountViewModel
    private lateinit var issueSavedFilterViewModel: IssueSavedFilterViewModel
    private lateinit var filterUpdatedViewModel: FilterUpdatedViewModel
    private lateinit var updateIssueListViewModel: UpdateIssueListViewModel

    @Inject
    lateinit var issueListRouter: IssueListRouter

    private val issues = mutableListOf<Issue>()
    private var filterReq = ""
    private var sortReq = ""
    private var fullIssueCount = 0

    private val endlessScrollListener = object : EndlessScrollListener({
        val sizeOfIssue = issueListRvAdapter.filter { it is IssueRvItem || it is IssueCompactRvItem || it is IssueDetailedRvItem }.size
        val formattedReq = buildReq()
        issueListViewModel.doAsyncRequest(IssueListParam(filter = formattedReq,skip = sizeOfIssue,top = DEFAULT_ISSUE_LIST_SIZE))
    }) {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> fabAddIssue.show()
                else -> fabAddIssue.hide()
            }
            super.onScrollStateChanged(recyclerView, newState)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IssueListDiProvider.getInstance().injectFragment(this)
        issueListViewModel = ViewModelProviders.of(this,viewModelFactory)[IssueListViewModel::class.java]
        issueListTypeViewModel = ViewModelProviders.of(this,viewModelFactory)[IssueListTypeViewModel::class.java]
        issueCountViewModel = ViewModelProviders.of(this,viewModelFactory)[IssueCountViewModel::class.java]
        issueSavedFilterViewModel = ViewModelProviders.of(this,viewModelFactory)[IssueSavedFilterViewModel::class.java]
        filterUpdatedViewModel = ViewModelProviders.of(this,viewModelFactory)[FilterUpdatedViewModel::class.java]
        updateIssueListViewModel = ViewModelProviders.of(this,viewModelFactory)[UpdateIssueListViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRv()
        initFilter()

        fabAddIssue.setOnClickListener {
        }
        imgIssueListSwitcher.setOnClickListener {
            issueListTypeViewModel.doAsyncRequest()
        }

        registerHandler(ViewState.Success::class.java,updateIssueListViewModel){
            issueSavedFilterViewModel.doAsyncRequest()
        }

        registerHandler(ViewState.Success::class.java,issueListTypeViewModel){
            updateListType(it.data as IssueListType)
            issueListRvAdapter.clear()
            updateAdapter(issues)
        }

        registerHandler(ViewState.Error::class.java,issueListViewModel){
            pbIssueList.visibility = View.GONE
            endlessScrollListener.loading = false
        }
        registerHandler(ViewState.Success::class.java,issueListViewModel){
            pbIssueList.visibility = View.GONE
            val issues = it.data as List<Issue>
            endlessScrollListener.loading = false
            if(issues.size < DEFAULT_ISSUE_LIST_SIZE){
                endlessScrollListener.endOfList = true
            }
            this.issues.addAll(issues)
            updateAdapter(issues)
        }

        registerHandler(ViewState.Error::class.java,issueCountViewModel){
            tvFilterCountBody.visibility = View.INVISIBLE
        }
        registerHandler(ViewState.Success::class.java,issueCountViewModel){
            fullIssueCount = (it.data as IssueCountDTO).value
            tvFilterCountBody.text = getString(R.string.issues_list_count_format,fullIssueCount.toString())
            tvFilterCountBody.visibility = View.VISIBLE
        }

        registerHandler(ViewState.Success::class.java,issueSavedFilterViewModel){
            val result = it.data as IssueSavedFilterViewModel.IssueFilterResultDTO
            filterReq = result.filterReq
            sortReq = result.sortReq
            if(sortReq.isNotEmpty()){
                tvFilterSortBody.text = sortReq
                tvFilterSortBody.visibility = View.VISIBLE
            }else{
                tvFilterSortBody.text = ""
                tvFilterSortBody.visibility = View.GONE
            }
            filterReq.trim()
            sortReq.trim()
            requestIssueList()
        }

        issueListTypeViewModel.doAsyncRequest()
        issueSavedFilterViewModel.doAsyncRequest()
    }

    private fun requestIssueList(){
        val formattedReq = buildReq()
        fullIssueCount = 0
        issueCountViewModel.doAsyncRequest(formattedReq)
        issueListViewModel.doAsyncRequest(IssueListParam(filter = formattedReq))
        issues.clear()
        issueListRvAdapter.clear()
        pbIssueList.visibility = View.VISIBLE
    }

    private fun buildReq(): String {
        val formattedReq = if (sortReq.isNotEmpty()) {
            "$filterReq ${getString(R.string.base_sort_by)}:$sortReq"
        } else {
            filterReq
        }
        formattedReq.trim()
        return formattedReq
    }

    private fun updateAdapter(issueList: List<Issue>){
        val map = linkedMapOf<String,MutableList<Issue>>()
        issueList.forEach {
            val hashMapKey = it.mappedUpdatedDate?.toFormattedString() ?: Date().toFormattedString()
            if (map.containsKey(hashMapKey)) {
                val list = map[hashMapKey]
                list!!.add(it)
            } else {
                val list = mutableListOf<Issue>()
                list.add(it)
                map[hashMapKey] = list
            }
        }
        val consolidatedList = mutableListOf<BaseRvItem>()
        for (date in map.keys) {
            val tmpList = map[date]
            val dateItem = TextRvItem(date)
            consolidatedList.add(dateItem)
            consolidatedList.addAll(mapIssueRvItems(tmpList.orEmpty()))
        }
        if(issueList.isNotEmpty()){
            issueListRvAdapter.addAll(consolidatedList)
        }else{

        }
    }


    private fun mapIssueRvItems(tmpList: List<Issue>) : List<BaseRvItem> {
        return when(issueListTypeViewModel.issueListType){
            IssueListType.COMPACT_VIEW ->
                tmpList.map {
                    IssueCompactRvItem(
                        issueCommonRvData = IssueCommonRvData(
                            it.idReadable,
                            it.summary,
                            it.priority.value.name,
                            it.mappedUpdatedDate?.toHourAndMinutesString().orEmpty(),
                            it.watchers.hasStar,
                            it.priority.value.fieldColor,
                            it.isDone()),
                        issueStateRvData = IssueStateRvData(
                            it.type.value.name,
                            it.state.value.name,
                            it.assignee.value.name,
                            it.reporter.name,
                            it.type.value.fieldColor,
                            it.state.value.fieldColor))
                }
            IssueListType.SINGLE_VIEW ->
                tmpList.map {
                    IssueRvItem(
                        issueCommonRvData = IssueCommonRvData(
                            it.idReadable,
                            it.summary,
                            it.priority.value.name,
                            it.mappedUpdatedDate?.toHourAndMinutesString().orEmpty(),
                            it.watchers.hasStar,
                            it.priority.value.fieldColor,
                            it.isDone()))
                }
            IssueListType.DETAILED_VIEW ->
                tmpList.map {
                    IssueDetailedRvItem(
                        issueCommonRvData = IssueCommonRvData(it.idReadable,it.summary,
                            it.priority.value.name,
                            it.mappedUpdatedDate?.toHourAndMinutesString().orEmpty(),
                            it.watchers.hasStar,
                            it.priority.value.fieldColor,
                            it.isDone()),
                        issueStateRvData = IssueStateRvData(it.type.value.name,
                            it.state.value.name,
                            it.assignee.value.name,
                            it.reporter.name,
                            it.type.value.fieldColor,
                            it.state.value.fieldColor),
                        issueDetailsRvData = IssueDetailsRvData(it.comments.size.toString(),
                            it.spentTime.value.presentation,
                            it.description,
                            it.spentTime.value.minutes,
                            it.estimation.value.minutes)
                    )
                }
            else -> emptyList()
        }

    }

    private fun updateListType(currentListType: IssueListType){
        val drwId = when(currentListType){
            IssueListType.SINGLE_VIEW -> {
                R.drawable.drw_single_view_filter
            }
            IssueListType.COMPACT_VIEW -> {
                R.drawable.drw_compact_view_filter
            }
            else -> {
                R.drawable.drw_detailed_view_filter
            }
        }
        imgIssueListSwitcher.setImageDrawable(ContextCompat.getDrawable(checkNotNull(context), drwId))
    }

    private fun initRv() {
        issueListRvAdapter = BaseRvAdapter(object : BaseRvClickListener {
            override fun onItemClickListener(rvItem: BaseRvItem) {
            }
        })

        rvIssues.layoutManager = LinearLayoutManager(context)
        val smallMargin = DeviceUtils.convertDpToPixel(8f, checkNotNull(context))
        val largeMargin = DeviceUtils.convertDpToPixel(16f, checkNotNull(context))
        rvIssues.addItemDecoration(IssueItemDecoration(smallMargin, smallMargin,largeMargin,largeMargin))
        rvIssues.adapter = issueListRvAdapter
        rvIssues.addOnScrollListener(endlessScrollListener)
    }

    private fun initFilter() {
        val openFilterClickListener = View.OnClickListener {
            issueListRouter.showFilter(fullIssueCount)
        }
        val openSortClickListener = View.OnClickListener {
            issueListRouter.showSort()
        }
        tvFilterCountHeader.setOnClickListener(openFilterClickListener)
        tvFilterCountBody.setOnClickListener(openFilterClickListener)
        imgFilterCount.setOnClickListener(openFilterClickListener)

        tvFilterSortBody.setOnClickListener(openSortClickListener)
        tvFilterSortHeader.setOnClickListener(openSortClickListener)
        imgFilterSort.setOnClickListener(openSortClickListener)

    }

}