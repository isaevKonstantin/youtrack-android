package com.konstantinisaev.youtrack.issuefilter

import android.os.Bundle
import android.view.View
import com.konstantinisaev.youtrack.issuefilter.di.IssueFilterDiProvider
import com.konstantinisaev.youtrack.ui.base.screens.BaseFragment
import kotlinx.android.synthetic.main.fragment_issue_auto_complete_filter.*

class IssueAutoCompleteFilterFragment : BaseFragment(){

    override val layoutId = R.layout.fragment_issue_auto_complete_filter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IssueFilterDiProvider.getInstance().injectFragment(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarWithBackNavigation(tlbSearch,getString(R.string.base_search))
    }
}