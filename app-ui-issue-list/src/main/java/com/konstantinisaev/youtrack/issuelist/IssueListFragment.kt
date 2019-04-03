package com.konstantinisaev.youtrack.issuelist

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.konstantinisaev.youtrack.core.api.DEFAULT_ISSUE_LIST_SIZE
import com.konstantinisaev.youtrack.core.rv.*
import com.konstantinisaev.youtrack.issuelist.di.IssueListDiProvider
import com.konstantinisaev.youtrack.issuelist.viewmodels.IssueListParam
import com.konstantinisaev.youtrack.issuelist.viewmodels.IssueListViewModel
import com.konstantinisaev.youtrack.ui.base.models.Issue
import com.konstantinisaev.youtrack.ui.base.screens.BaseFragment
import com.konstantinisaev.youtrack.ui.base.utils.DeviceUtils
import com.konstantinisaev.youtrack.ui.base.utils.toFormattedString
import com.konstantinisaev.youtrack.ui.base.utils.toHourAndMinutesString
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import kotlinx.android.synthetic.main.fragment_issue_list.*
import java.util.*

class IssueListFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_issue_list

    private lateinit var issueListRvAdapter: BaseRvAdapter
    lateinit var issueListViewModel: IssueListViewModel

    private var filterReq = ""
    private var sortReq = ""
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRv()
        initFilter(view)
        fabAddIssue.setOnClickListener {
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
            updateAdapter(issues)
        }
        savedInstanceState?: requestIssueList()
    }

    private fun initFilter(view: View) {
//        issueFilterViewController = IssueFilterViewController(view, PreferenceHelper.getInstance(context!!).getIssueListType(), this)
//        filterReq = preferenceHelper.getSavedQuery()
//        sortReq = preferenceHelper.getSortQuery()
//        if(sortReq.isNotEmpty()){
//            tvFilterSortBody.text = sortReq
//            tvFilterSortBody.visibility = View.VISIBLE
//        }else{
//            tvFilterSortBody.text = ""
//            tvFilterSortBody.visibility = View.GONE
//        }
//        filterReq.trim()
//        sortReq.trim()

    }

    private fun requestIssueList(){
        val formattedReq = buildReq()
//        issueListViewModel.getAllIssuesCount(filter = formattedReq)
        issueListViewModel.doAsyncRequest(IssueListParam(filter = formattedReq))
//        issues.clear()
        issueListRvAdapter.clear()
        pbIssueList.visibility = View.VISIBLE
    }

    private fun buildReq(): String {
        val formattedReq = if (sortReq.isNotEmpty()) {
            "$filterReq ${getString(R.string.common_sort_by)}:$sortReq"
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
        return tmpList.map {
            IssueRvItem(
                issueCommonRvData = IssueCommonRvData(
                    it.idReadable,
                    it.summary,
                    it.priority.value.name,
                    it.mappedUpdatedDate?.toHourAndMinutesString().orEmpty(),
                    it.watchers.hasStar,
                    it.priority.value.fieldColor,
                    it.isDone()
                )
            )
        }
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
}