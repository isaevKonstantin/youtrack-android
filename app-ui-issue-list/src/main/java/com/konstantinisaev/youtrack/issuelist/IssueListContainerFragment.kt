package com.konstantinisaev.youtrack.issuelist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.konstantinisaev.youtrack.issuelist.di.IssueListDiProvider
import com.konstantinisaev.youtrack.ui.base.screens.BaseFragment
import com.konstantinisaev.youtrack.ui.base.utils.IssueListRouter
import com.konstantinisaev.youtrack.ui.base.utils.MainRouter
import com.konstantinisaev.youtrack.ui.base.viewmodels.FilterUpdatedViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import kotlinx.android.synthetic.main.fragment_issue_list_container.*
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Inject

class IssueListContainerFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_issue_list_container

    private var leftToggle: ActionBarDrawerToggle? = null

    @Inject
    lateinit var mainRouter: MainRouter

    @Inject
    lateinit var issueListRouter: IssueListRouter
    lateinit var filterUpdatedViewModel: FilterUpdatedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IssueListDiProvider.getInstance().injectFragment(this)
        filterUpdatedViewModel = getViewModel(FilterUpdatedViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarWithBackNavigation(toolbar,getString(R.string.nav_rv_issues))
        initDrawer()
        mainRouter.setNavigator(SupportAppNavigator(activity,childFragmentManager,R.id.flContainer))
        issueListRouter.setNavigator(SupportAppNavigator(activity,childFragmentManager,R.id.flFilter))
        childFragmentManager.addOnBackStackChangedListener {
            if(layDrawer.isDrawerOpen(GravityCompat.START)){
                closeLeftDrawer()
                updateToolbar(childFragmentManager.findFragmentById(R.id.flContainer))
            }else if(!layDrawer.isDrawerOpen(GravityCompat.END) && childFragmentManager.findFragmentById(R.id.flFilter) != null){
                layDrawer.openDrawer(GravityCompat.END)
            }
        }
        registerHandler(ViewState.Success::class.java,filterUpdatedViewModel){
            closeRightDrawer()
        }
        savedInstanceState ?: mainRouter.showIssueList()
    }

    private fun initDrawer() {
        leftToggle = object : ActionBarDrawerToggle(
            activity, layDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(nvMain)
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(nvMain)
            }
        }
        val rightToggle = object : ActionBarDrawerToggle(
            activity, layDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(flFilter)
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(flFilter)
            }
        }
        leftToggle!!.isDrawerIndicatorEnabled = true
        layDrawer.addDrawerListener(leftToggle!!)
        layDrawer.addDrawerListener(rightToggle)
        leftToggle!!.syncState()

    }

    private fun updateToolbar(fragment: Fragment?){
        fragment ?: let { return }
        when(fragment){
            is IssueListFragment -> {
//                val savedQuery = PreferenceHelper.getInstance(this).getSavedQuery()
//                if(savedQuery.isNotEmpty()){
//                    supportActionBar?.title = savedQuery
//                }else{
//                    supportActionBar?.title = getString(R.string.nav_rv_issues)
//                }
                toolbar.title = getString(R.string.nav_rv_issues)

            }
            is AgileBoardsFragment -> toolbar.title = getString(R.string.nav_rv_agile_boards)
            is SettingsFragment -> toolbar.title = getString(R.string.nav_rv_settings)
            is AboutFragment -> toolbar.title = getString(R.string.nav_rv_about)
        }
    }

    fun closeLeftDrawer() {
        layDrawer.closeDrawer(GravityCompat.START)
    }

    fun closeRightDrawer() {
        layDrawer.closeDrawer(GravityCompat.END)
    }


}