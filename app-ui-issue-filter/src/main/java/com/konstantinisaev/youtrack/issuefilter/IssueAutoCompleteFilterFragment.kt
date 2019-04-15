package com.konstantinisaev.youtrack.issuefilter

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.konstantinisaev.youtrack.core.rv.*
import com.konstantinisaev.youtrack.issuefilter.di.IssueFilterDiProvider
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.models.IssueFilterSuggest
import com.konstantinisaev.youtrack.ui.base.screens.BaseFragment
import com.konstantinisaev.youtrack.ui.base.utils.DeviceUtils
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import com.konstantinisaev.youtrack.ui.base.widget.afterTextChanged
import kotlinx.android.synthetic.main.fragment_issue_auto_complete_filter.*
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class IssueAutoCompleteFilterFragment : BaseFragment(){

    override val layoutId = R.layout.fragment_issue_auto_complete_filter

    private lateinit var filterViewModel: IssueServerFilterViewModel
    private lateinit var baseRvAdapter: BaseRvAdapter
    @Inject
    lateinit var basePreferencesAdapter: BasePreferencesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IssueFilterDiProvider.getInstance().injectFragment(this)
        filterViewModel = getViewModel(IssueServerFilterViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarWithBackNavigation(tlbSearch,getString(R.string.base_search))
        initRv()
        initSearchEdt()

        registerHandler(ViewState.Error::class.java,filterViewModel){
            showError(it)
        }
        registerHandler(ViewState.Success::class.java,filterViewModel){
            val data = it.data as List<IssueFilterSuggest>
            baseRvAdapter.clear()
            baseRvAdapter.addAll(data.map { IssueFullSearchSuggestionRvItem(it.option.orEmpty(),it.prefix.orEmpty(),it.suffix.orEmpty()) })
        }

        basePreferencesAdapter.getSavedQuery().takeIf { it.isNotEmpty() }?.let {
            edtAutoCompleteFilter.setText(it)
            edtAutoCompleteFilter.setSelection(edtAutoCompleteFilter.text.length)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.issue_auto_complete_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.onBackPressed()
            R.id.mConfirm -> confirmSearchQuery()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initSearchEdt() {
        edtAutoCompleteFilter.afterTextChanged {
            if(it.isEmpty()){
                baseRvAdapter.clear()
                return@afterTextChanged
            }
            filterViewModel.doAsyncRequest(IssueServerFilterViewModel.IssueServerFilterParam(query = it, caret = it.length))
        }
        edtAutoCompleteFilter.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                confirmSearchQuery()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun initRv() {
        rvAutoCompleteFilter.layoutManager = LinearLayoutManager(context)
        rvAutoCompleteFilter.addItemDecoration(MarginItemDecoration(
            DeviceUtils.convertDpToPixel(10f, checkNotNull(context)),
            DeviceUtils.convertDpToPixel(10f, checkNotNull(context)),
            DeviceUtils.convertDpToPixel(16f, checkNotNull(context)),
            DeviceUtils.convertDpToPixel(16f, checkNotNull(context))
        ))
        baseRvAdapter = BaseRvAdapter(object : BaseRvClickListener {
            override fun onItemClickListener(rvItem: BaseRvItem) {
                if(rvItem is IssueFullSearchSuggestionRvItem){
                    edtAutoCompleteFilter.setText(filterViewModel.buildQueryString(edtAutoCompleteFilter.text.toString(),rvItem.option,rvItem.prefix,rvItem.suffix))
                    edtAutoCompleteFilter.setSelection(edtAutoCompleteFilter.text.length)
                }
            }
        })
        rvAutoCompleteFilter.adapter = baseRvAdapter
    }

    private fun confirmSearchQuery(){
        val query = edtAutoCompleteFilter.text.toString()
        filterViewModel.saveFilterReq(query)
        activity?.onBackPressed()
    }

}