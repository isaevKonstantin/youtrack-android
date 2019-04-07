package com.konstantinisaev.youtrack.issuelist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.konstantinisaev.youtrack.core.api.CurrentUserDTO
import com.konstantinisaev.youtrack.core.rv.*
import com.konstantinisaev.youtrack.issuelist.di.IssueListDiProvider
import com.konstantinisaev.youtrack.issuelist.viewmodels.ProfileViewModel
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.screens.BaseFragment
import com.konstantinisaev.youtrack.ui.base.utils.DeviceUtils
import com.konstantinisaev.youtrack.ui.base.utils.MainRouter
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import kotlinx.android.synthetic.main.fragment_navigation.*
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class NavigationMenuFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_navigation

    private lateinit var navRvAdapter: BaseRvAdapter
    private lateinit var profileViewModel: ProfileViewModel
    @Inject
    lateinit var mainRouter: MainRouter
    @Inject
    lateinit var basePreferencesAdapter: BasePreferencesAdapter

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
                onChangeNavigationFragment(textRvItem.text)
            }
        })
        rvNavigation.addItemDecoration(MarginItemDecoration(
            topMargin = DeviceUtils.convertDpToPixel(8f, checkNotNull(context)),
            bottomMargin = DeviceUtils.convertDpToPixel(8f,checkNotNull(context))))

        rvNavigation.adapter = navRvAdapter
        val navTextItems = resources.getStringArray(R.array.nav_items).map { NavTextRvItem(it) }
        val items = mutableListOf<BaseRvItem>(NavProfileRvItem())
        items.addAll(navTextItems)
        navRvAdapter.addAll(items)

        registerHandler(ViewState.Error::class.java,profileViewModel) {
            showError(it)
        }
        registerHandler(ViewState.Success::class.java,profileViewModel) {
            val userDTO = it.data as CurrentUserDTO
            navRvAdapter.update(0,NavProfileRvItem(userDTO.fullName.orEmpty(),userDTO.initials,userDTO.formattedImageUrl))
        }
        profileViewModel.doAsyncRequest()
    }

    @SuppressLint("RtlHardcoded")
    fun onChangeNavigationFragment(text: String){
        when(text){
            getString(R.string.nav_rv_issues) -> mainRouter.showIssueList()
            getString(R.string.nav_rv_agile_boards) -> mainRouter.showAgileBoards()
            getString(R.string.nav_rv_settings) -> mainRouter.showSettings()
            getString(R.string.nav_rv_about) -> mainRouter.showAbout()
            getString(R.string.nav_rv_logout) -> {
                val fragmentManager = activity?.supportFragmentManager ?: return
                mainRouter.setNavigator(SupportAppNavigator(activity,fragmentManager,R.id.flContainer))
                basePreferencesAdapter.setAuthToken(null)
                basePreferencesAdapter.setServerConfig(null)
                mainRouter.showServerUrl()
            }
            else -> throw IllegalArgumentException("Wrong text in nav rv item: $text")
        }
    }
}