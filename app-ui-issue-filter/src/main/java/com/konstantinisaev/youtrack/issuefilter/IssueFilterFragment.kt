package com.konstantinisaev.youtrack.issuefilter

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.konstantinisaev.youtrack.core.api.IssueCountDTO
import com.konstantinisaev.youtrack.core.rv.*
import com.konstantinisaev.youtrack.issuefilter.di.IssueFilterDiProvider
import com.konstantinisaev.youtrack.ui.base.models.IssueFilterSuggest
import com.konstantinisaev.youtrack.ui.base.screens.BaseFragment
import com.konstantinisaev.youtrack.ui.base.utils.DeviceUtils
import com.konstantinisaev.youtrack.ui.base.utils.Extra
import com.konstantinisaev.youtrack.ui.base.viewmodels.IssueCountViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import kotlinx.android.synthetic.main.fragment_issue_filter.*

@Suppress("MemberVisibilityCanBePrivate", "UNCHECKED_CAST")
class IssueFilterFragment : BaseFragment(){

    override val layoutId = R.layout.fragment_issue_filter

    private var initialCount = 0
    lateinit var issueServerFilterViewModel : IssueServerFilterViewModel
    lateinit var issueCountViewModel: IssueCountViewModel
    private lateinit var issueFilterRvAdapter: BaseRvAdapter
    private val adapterList = mutableListOf<BaseRvItem>()
    private val checkedItemsUuid = hashSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IssueFilterDiProvider.getInstance().injectFragment(this)
        issueServerFilterViewModel = ViewModelProviders.of(this,viewModelFactory)[IssueServerFilterViewModel::class.java]
        issueCountViewModel = ViewModelProviders.of(this,viewModelFactory)[IssueCountViewModel::class.java]
        initialCount = arguments?.getInt(Extra.ISSUE_COUNT) ?: 0

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRv()
        initButtons()
        tlbFilter.setTitle(R.string.issues_list_filter)
        if(initialCount > 0){
            tlbFilter.subtitle = getString(R.string.issues_list_count_format,initialCount.toString())
        }

        registerHandler(ViewState.Error::class.java,issueCountViewModel){
            showError(it)
        }
        registerHandler(ViewState.Success::class.java,issueCountViewModel){
            val count = (it.data as IssueCountDTO).value
            vFilterSplash.visibility = View.GONE
            pbFilterIssue.visibility = View.GONE
            tlbFilter.subtitle = getString(R.string.issues_list_count_format,count.toString())
        }

        registerHandler(ViewState.Error::class.java,issueServerFilterViewModel){
            showError(it)
        }
        registerHandler(ViewState.Success::class.java,issueServerFilterViewModel){viewState ->
            val data = viewState.data as List<IssueFilterSuggest>
            if (issueFilterRvAdapter.isEmpty()) {
                issueFilterRvAdapter.addAll(data.asSequence().map { IssueFilterSuggestionRvItem(IssueFilterSuggestionRvData(it.option.orEmpty(), it.uuid, false)) }.toList())
            } else {
                val parentUuid = data.firstOrNull()?.parentUuid.orEmpty()
                if (parentUuid.isNotEmpty()) {
                    val position = issueFilterRvAdapter.findPosition { (it is IssueFilterSuggestionRvItem) && it.issueFilterSuggestionRvData.uuid == parentUuid }
                    position.takeIf { it >= 0 }?.let { addedPosition ->
                        val insertPosition = addedPosition.inc()
                        if(data.isNotEmpty()){
                            issueFilterRvAdapter.addAllByPosition(insertPosition, data.map { IssueFilterSuggestionChildRvItem(parentUuid, IssueFilterSuggestionRvData(it.option.orEmpty(), it.uuid, checkedItemsUuid.contains(it.uuid))) })
                        }else{
                            issueFilterRvAdapter.addByPosition(insertPosition,IssueFilterSuggestionEmpty(parentUuid))
                        }
                    }
                }
            }
            adapterList.clear()
            adapterList.addAll(issueFilterRvAdapter.getAll())
        }

        issueServerFilterViewModel.doAsyncRequest()
    }

    @Suppress("UNCHECKED_CAST")
    private fun initRv() {
        issueFilterRvAdapter = BaseRvAdapter(object : BaseRvClickListener {
            override fun onItemClickListener(rvItem: BaseRvItem) {
                if(rvItem is IssueFilterSuggestionRvItem){
                    if(rvItem.issueFilterSuggestionRvData.checked){
                        issueServerFilterViewModel.doAsyncRequest(IssueServerFilterViewModel.IssueServerFilterParam(query = "${rvItem.issueFilterSuggestionRvData.name}:",parentUuid = rvItem.issueFilterSuggestionRvData.uuid))
                    }else{
                        issueFilterRvAdapter.removeAll{
                            (it is IssueFilterSuggestionChildRvItem && it.parentUuid == rvItem.issueFilterSuggestionRvData.uuid) ||
                                    (it is IssueFilterSuggestionEmpty && it.parentUuid == rvItem.issueFilterSuggestionRvData.uuid)
                        }
                    }
                }else if(rvItem is IssueFilterSuggestionChildRvItem){
                    val filterReq = issueServerFilterViewModel.formatCheckedValuesToStr(issueFilterRvAdapter.filter { it is IssueFilterSuggestionChildRvItem && it.issueFilterSuggestionRvData.checked} as List<IssueFilterSuggestionChildRvItem>)
                    if(rvItem.issueFilterSuggestionRvData.checked){
                        checkedItemsUuid.add(rvItem.issueFilterSuggestionRvData.uuid)
                        vFilterSplash.visibility = View.VISIBLE
                        pbFilterIssue.visibility = View.VISIBLE
                        issueCountViewModel.doAsyncRequest(filterReq)
                    }else{
                        checkedItemsUuid.remove(rvItem.issueFilterSuggestionRvData.uuid)
                        if(filterReq.isNotEmpty()){
                            vFilterSplash.visibility = View.VISIBLE
                            pbFilterIssue.visibility = View.VISIBLE
                            issueCountViewModel.doAsyncRequest(filterReq)
                        }else{
                            if(initialCount > 0){
                                tlbFilter.subtitle = getString(R.string.issues_list_count_format,initialCount.toString())
                            }
                        }
                    }
                }
            }
        })

        rvIssueFilter.layoutManager = LinearLayoutManager(context)
        rvIssueFilter.addItemDecoration(FilterItemDecoration(
            leftMargin = DeviceUtils.convertDpToPixel(20f, checkNotNull(context)),
            rightMargin = DeviceUtils.convertDpToPixel(10f, checkNotNull(context)),
            topMargin = DeviceUtils.convertDpToPixel(5f, checkNotNull(context)),
            bottomMargin = DeviceUtils.convertDpToPixel(5f, checkNotNull(context))))
        rvIssueFilter.adapter = issueFilterRvAdapter
    }

    @Suppress("UNCHECKED_CAST")
    private fun initButtons() {
        tvResetFilterView.setOnClickListener {
            tlbFilter.subtitle = getString(R.string.issues_list_count_format, initialCount.toString())
            issueFilterRvAdapter.filter { it is IssueFilterSuggestionChildRvItem }.forEach { (it as IssueFilterSuggestionChildRvItem).issueFilterSuggestionRvData.checked = false }
            issueFilterRvAdapter.notifyDataSetChanged()
        }
        tvSubmitFilter.setOnClickListener {
        }
    }


}