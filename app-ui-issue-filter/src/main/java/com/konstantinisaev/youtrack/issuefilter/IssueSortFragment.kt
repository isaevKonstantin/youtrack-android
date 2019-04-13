package com.konstantinisaev.youtrack.issuefilter

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.konstantinisaev.youtrack.core.rv.*
import com.konstantinisaev.youtrack.issuefilter.di.IssueFilterDiProvider
import com.konstantinisaev.youtrack.ui.base.models.IssueFilterSuggest
import com.konstantinisaev.youtrack.ui.base.screens.BaseFragment
import com.konstantinisaev.youtrack.ui.base.utils.DeviceUtils
import com.konstantinisaev.youtrack.ui.base.viewmodels.FilterUpdatedViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.UpdateIssueListViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import kotlinx.android.synthetic.main.fragment_issue_sort.*

@Suppress("UNCHECKED_CAST")
class IssueSortFragment : BaseFragment(){

    override val layoutId = R.layout.fragment_issue_sort

    private lateinit var issueSortRvAdapter: BaseRvAdapter

    private lateinit var filterUpdatedViewModel: FilterUpdatedViewModel
    private lateinit var issueServerFilterViewModel : IssueServerFilterViewModel
    private lateinit var updateIssueListViewModel: UpdateIssueListViewModel
    private val adapterMap = mutableMapOf<String, IssueFilterSuggest>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IssueFilterDiProvider.getInstance().injectFragment(this)
        filterUpdatedViewModel = ViewModelProviders.of(this,viewModelFactory)[FilterUpdatedViewModel::class.java]
        issueServerFilterViewModel = ViewModelProviders.of(this,viewModelFactory)[IssueServerFilterViewModel::class.java]
        updateIssueListViewModel = ViewModelProviders.of(this,viewModelFactory)[UpdateIssueListViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tlbSort.title = getString(R.string.base_sort)
        initRv()

        registerHandler(ViewState.Error::class.java,issueServerFilterViewModel){
            pbSortIssue.visibility = View.GONE
            showError(it)
        }
        registerHandler(ViewState.Success::class.java,issueServerFilterViewModel){viewState ->
            pbSortIssue.visibility = View.GONE
            val data = viewState.data as List<IssueFilterSuggest>
            issueSortRvAdapter.addAll(data.asSequence().map { IssueFilterSuggestionChildRvItem("",IssueFilterSuggestionRvData(it.option.orEmpty(), it.uuid, false)) }.toList())
            adapterMap.clear()
            data.forEach {
                adapterMap[it.uuid] = it
            }
        }

        savedInstanceState ?: let {
            if(adapterMap.isNotEmpty()){
                issueSortRvAdapter.addAll(adapterMap.values.map { IssueFilterSuggestionChildRvItem("",IssueFilterSuggestionRvData(it.option.orEmpty(), it.uuid, false)) })
            }else{
                pbSortIssue.visibility = View.VISIBLE
                issueServerFilterViewModel.doAsyncRequest(IssueServerFilterViewModel.IssueServerFilterParam(query = "${getString(R.string.base_sort_by)}:",optionsLimit = 30))
            }
        }

    }

    private fun initRv(){
        issueSortRvAdapter = BaseRvAdapter(object : BaseRvClickListener {
            override fun onItemClickListener(rvItem: BaseRvItem) {
                if(rvItem is IssueFilterSuggestionChildRvItem){
                    val sortedStr = issueSortRvAdapter.getAll().asSequence().
                        filter { (it as IssueFilterSuggestionChildRvItem).issueFilterSuggestionRvData.checked }.
                        joinToString {item ->
                            var itemFullOption = ""
                            val uuid = (item as IssueFilterSuggestionChildRvItem).issueFilterSuggestionRvData.uuid
                            adapterMap[uuid]?.let {
                                itemFullOption += if(it.option == "star"){
                                    "tag: ${it.prefix.orEmpty()}${it.option.orEmpty()}${it.suffix.orEmpty()}"
                                }else{
                                    "${it.prefix.orEmpty()}${it.option.orEmpty()}${it.suffix.orEmpty()}"
                                }
                            }
                            itemFullOption
                        }
                    issueServerFilterViewModel.setSortQuery(sortedStr)
                    updateIssueListViewModel.changeViewState(ViewState.Success(updateIssueListViewModel::class.java,""))
                }
            }
        })
        rvIssueSort.layoutManager = LinearLayoutManager(context)
        rvIssueSort.addItemDecoration(
            MarginItemDecoration(
                leftMargin = DeviceUtils.convertDpToPixel(16f, checkNotNull(context)),
                rightMargin = DeviceUtils.convertDpToPixel(16f, checkNotNull(context)),
                topMargin = DeviceUtils.convertDpToPixel(5f, checkNotNull(context)),
                bottomMargin = DeviceUtils.convertDpToPixel(5f, checkNotNull(context)))
        )
        rvIssueSort.adapter = issueSortRvAdapter
    }

}