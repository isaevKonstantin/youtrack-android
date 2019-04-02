package com.konstantinisaev.youtrack.issuelist

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.konstantinisaev.youtrack.core.api.CurrentUserDTO
import com.konstantinisaev.youtrack.core.rv.*
import com.konstantinisaev.youtrack.issuelist.di.IssueListDiProvider
import com.konstantinisaev.youtrack.ui.base.screens.BaseFragment
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import kotlinx.android.synthetic.main.fragment_navigation.*

@Suppress("UNCHECKED_CAST")
class NavigationMenuFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_navigation

    private lateinit var navRvAdapter: BaseRvAdapter
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IssueListDiProvider.getInstance().injectFragment(this)
        profileViewModel = ViewModelProviders.of(this,viewModelFactory)[ProfileViewModel::class.java]
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
        val items = mutableListOf<BaseRvItem>(NavProfileRvItem())
        items.addAll(navTextItems)
        navRvAdapter.addAll(items)

        registerHandler(ViewState.Error::class.java,profileViewModel) {
            showError(it as ViewState.Error)
        }
        registerHandler(ViewState.Success::class.java,profileViewModel) {
            val userDTO = ((it as ViewState.Success<CurrentUserDTO>).data)
            navRvAdapter.update(0,NavProfileRvItem(userDTO.fullName.orEmpty(),userDTO.initials,userDTO.formattedImageUrl))
        }
        profileViewModel.doAsyncRequest()
    }
}