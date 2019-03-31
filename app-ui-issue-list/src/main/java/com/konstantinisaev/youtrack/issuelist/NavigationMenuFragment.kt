package com.konstantinisaev.youtrack.issuelist

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.konstantinisaev.youtrack.core.rv.BaseRvAdapter
import com.konstantinisaev.youtrack.core.rv.BaseRvClickListener
import com.konstantinisaev.youtrack.core.rv.BaseRvItem
import com.konstantinisaev.youtrack.core.rv.NavTextRvItem
import com.konstantinisaev.youtrack.ui.base.screens.BaseFragment
import kotlinx.android.synthetic.main.fragment_navigation.*

class NavigationMenuFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_navigation

    private lateinit var navRvAdapter: BaseRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvNavigation.layoutManager = LinearLayoutManager(context)
        navRvAdapter = BaseRvAdapter(object : BaseRvClickListener {
            override fun onItemClickListener(rvItem: BaseRvItem) {
                val textRvItem = rvItem as NavTextRvItem
                val parentActivity = activity
//                if(parentActivity is MainView){
//                    parentActivity.onChangeNavigationFragment(textRvItem.text)
//                }
            }
        })
//        rvNavigation.addItemDecoration(NavItemDecoration())
        rvNavigation.adapter = navRvAdapter
        val navTextItems = resources.getStringArray(R.array.nav_items).map { NavTextRvItem(it) }
        navRvAdapter.addAll(navTextItems)
    }
}