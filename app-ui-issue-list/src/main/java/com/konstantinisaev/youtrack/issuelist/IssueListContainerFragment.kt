package com.konstantinisaev.youtrack.issuelist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.konstantinisaev.youtrack.ui.base.screens.BaseFragment
import kotlinx.android.synthetic.main.fragment_issue_list_container.*

class IssueListContainerFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_issue_list_container

    private var mToggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarWithBackNavigation(toolbar,getString(R.string.nav_rv_issues))
        initDrawer()
    }

    private fun initDrawer() {
        mToggle = object : ActionBarDrawerToggle(
            activity, layDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(nvMain)
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(nvMain)
            }
        }
        val toggle = object : ActionBarDrawerToggle(
            activity, layDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(flFilter)
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(flFilter)
            }
        }
        mToggle!!.isDrawerIndicatorEnabled = true
        layDrawer.addDrawerListener(mToggle!!)
        layDrawer.addDrawerListener(toggle)
        mToggle!!.syncState()
    }

    private fun updateToolbar(fragment: Fragment?){
        fragment ?: let { return }
//        when(fragment){
//            is IssueListFragment -> {
//                val savedQuery = PreferenceHelper.getInstance(this).getSavedQuery()
//                if(savedQuery.isNotEmpty()){
//                    supportActionBar?.title = savedQuery
//                }else{
//                    supportActionBar?.title = getString(R.string.nav_rv_issues)
//                }
//            }
//            is AgileBoardsFragment -> supportActionBar?.title = getString(R.string.nav_rv_agile_boards)
//            is SettingsFragment -> supportActionBar?.title = getString(R.string.nav_rv_settings)
//            is AboutFragment -> supportActionBar?.title = getString(R.string.nav_rv_about)
//        }
    }


}